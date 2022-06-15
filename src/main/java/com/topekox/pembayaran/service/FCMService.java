package com.topekox.pembayaran.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topekox.pembayaran.dao.AntrianFCMDao;
import com.topekox.pembayaran.entity.AntrianFCM;
import com.topekox.pembayaran.entity.Produk;
import com.topekox.pembayaran.entity.StatusAntrian;
import com.topekox.pembayaran.exception.RegisterTokenToTopicFailedException;
import com.topekox.pembayaran.fcm.NotificationRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Firebase Cloud Messaging API (V1) Read More:
 * {@link https://firebase.google.com/docs/cloud-messaging/migrate-v1}
 * 
 * @author ucup
 */

@Slf4j
@Service
public class FCMService {

	private static final String TAG = "FCM Service: ";

	@Value("${firebase.config-file}")
	private String firebaseConfigFile;
	private AntrianFCMDao antrianFCMDao;
	
	@Autowired
	public FCMService(AntrianFCMDao antrianFCMDao) {
		this.antrianFCMDao = antrianFCMDao;
	}

	@PostConstruct
	public void initialize() {

		try {
			log.info(TAG + "Load file: " + firebaseConfigFile);

			FirebaseOptions option = FirebaseOptions.builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigFile).getInputStream()))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(option);
				log.info(TAG + "Firebase App has been initialized");
			}
		} catch (IOException e) {
			log.error(TAG + e.getMessage());
		}
	}

	public void sendMessageToToken(NotificationRequest request) throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonOutput = gson.toJson(message);
		String response = sendAndGetResponse(message);
		log.info(
				"Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
	}
	
	public void sendMessageAndSaveToAntrian(NotificationRequest request) throws InterruptedException, ExecutionException {
		sendMessageToToken(request);
		saveToAntrianFCM(request);
	}
	
	public void sendMessageToTopic(Produk produk) {
		
		/* Mengirim message berupa notifikasi dan payload */
//		Message message = Message.builder()
//				.setNotification(Notification.builder()
//						.setTitle("Produk")
//						.setBody("Update Produk: " + produk.getNama())
//						.build())
//				.putData("action", "update")
//				.setTopic("produk")
//				.build();
		
		/* Mengirim message berupa payload */
		Message message = Message.builder()
				.putData("action", "update")
				.putData("message", "Hello World")
				.setTopic("produk")
				.build();
		
		try {
			String response = FirebaseMessaging.getInstance().send(message);
			if(response != null) {
				log.info("Sukses mengirim pesan: {}", response);
			} else {
				log.error("GAGAL mengirim pesan: {}", response);
			}
		} catch (FirebaseMessagingException e) {
			log.error(TAG + e.getMessage());
		}		
	}


	private void saveToAntrianFCM(NotificationRequest request) {
		AntrianFCM antrianFCM = new AntrianFCM();
		antrianFCM.setTitle(request.getTitle());
		antrianFCM.setMessage(request.getMessage());
		antrianFCM.setTokenTujuan(request.getToken());
		antrianFCM.setTopic(request.getTopic());

		antrianFCMDao.save(antrianFCM);
	}

	@Scheduled(fixedDelay = 5000)
	private void sendingProcess() {
		Pageable pageRequest = PageRequest.of(0, 1);
		Page<AntrianFCM> antrianTeratas = 
				antrianFCMDao.findByStatusAntrian(StatusAntrian.BARU, pageRequest);
		
		log.info(TAG + "Memproses {} antrian dari {}",
				antrianTeratas.getNumberOfElements(),
				antrianTeratas.getTotalElements());
		
		if (antrianTeratas.getNumberOfElements() < 1) {
			return;
		}
		
		AntrianFCM antrianFCM = antrianTeratas.getContent().get(0);
		log.info(TAG + antrianFCM.toString());
		
		NotificationRequest request = new NotificationRequest();
		request.setMessage(antrianFCM.getMessage());
		request.setTitle(antrianFCM.getTitle());
		request.setToken(antrianFCM.getTokenTujuan());
		request.setTopic(antrianFCM.getTopic());
		
		try {
			sendMessageToToken(request);
			log.info(TAG + "Sukses Mengirim Pesan...");
			antrianFCM.setStatusAntrian(StatusAntrian.TERKIRIM);
			antrianFCM.setWaktuKirim(new Date());
		} catch (Exception e) {
			log.error(TAG + e.getMessage());
			antrianFCM.setStatusAntrian(StatusAntrian.GAGAL_KIRIM);
			antrianFCM.setWaktuKirim(new Date());
			antrianFCM.setKeterangan(e.getMessage());			
		} finally {
			antrianFCMDao.save(antrianFCM);
		}	
	}
	
	public void registerTokenToTopics(String token, String topic) 
			throws RegisterTokenToTopicFailedException {
		
		log.info("Menerima topic {} dengan token {}", topic, token);
		
		try {
			log.info("Siap mendaftarkan token ke topic...");
			TopicManagementResponse response = FirebaseMessaging.getInstance()
					.subscribeToTopic(Arrays.asList(token), topic);
			
			if (response.getSuccessCount() == 1) {
				log.info("Berhasil mendaftarkan token ke topik {}", topic);
			} else if (response.getSuccessCount() == 0) {
				log.error("GAGAL mendaftarkan token ke topik {}", topic);
			}
			
		} catch (FirebaseMessagingException e) {
			log.error(e.getMessage());
		}
	}
	
	private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		return FirebaseMessaging.getInstance().sendAsync(message).get();
	}

	private AndroidConfig getAndroidConfig(String topic) {
		return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.HIGH)
				.setNotification(AndroidNotification.builder().setTag(topic).build()).build();
	}

	private ApnsConfig getApnsConfig(String topic) {
		return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
	}

	private Message getPreconfiguredMessageToToken(NotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
	}

	private Message getPreconfiguredMessageWithoutData(NotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
	}

	private Message getPreconfiguredMessageWithData(Map<String, String> data, NotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken()).build();
	}

	private Message.Builder getPreconfiguredMessageBuilder(NotificationRequest request) {
		AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

		Notification notification = Notification.builder().setBody(request.getMessage()).setTitle(request.getTitle())
				.build();

		return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
				.setNotification(notification);
	}

}
