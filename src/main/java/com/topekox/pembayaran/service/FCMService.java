package com.topekox.pembayaran.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topekox.pembayaran.fcm.NotificationRequest;

/**
 * Firebase Cloud Messaging API (V1) Read More:
 * {@link https://firebase.google.com/docs/cloud-messaging/migrate-v1}
 * 
 * @author ucup
 */

@Service
public class FCMService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FCMService.class);
	private static final String TAG = "FCM Service: ";

	@Value("${firebase.config-file}")
	private String firebaseConfigFile;

	// for Testing
	// change ClassPathResources to FileInputStream
	private static final String URL_FB_CONFIG = "src/main/resources/service-account.json";

	@PostConstruct
	public void initialize() {

		try {
			LOGGER.debug(TAG + "Load file: " + firebaseConfigFile);

			FirebaseOptions option = FirebaseOptions.builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigFile).getInputStream()))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(option);
				LOGGER.info(TAG + "Firebase App has been initialized");
			}
		} catch (IOException e) {
			LOGGER.error(TAG + e.getMessage());
		}
	}

	public void messageToToken(NotificationRequest request) {
		try {
			Message message = getPreconfiguredMessageToToken(request);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonOutput = gson.toJson(message);
			String response = sendAndGetResponse(message);
			LOGGER.info("Sent message to token. Device token: " + request.getToken() + ", " + response + " msg "
					+ jsonOutput);
		} catch (ExecutionException | InterruptedException e) {
			LOGGER.error(TAG + e.getMessage());
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
