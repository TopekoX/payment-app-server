package com.topekox.pembayaran;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.topekox.pembayaran.dao.AntrianFCMDao;
import com.topekox.pembayaran.fcm.NotificationRequest;
import com.topekox.pembayaran.service.FCMService;

@SpringBootTest
public class FCMServiceTest {
	
	private static FirebaseOptions option;
	@Autowired private AntrianFCMDao antrianFCMDao; 
	
	@BeforeAll
	public static void beforeAll() {
		try {	
			option = FirebaseOptions.builder()
					.setCredentials(
							GoogleCredentials.fromStream(
									new FileInputStream(
											"src/main/resources/my-firebase-with-spring-boot-firebase-adminsdk-pembayaran.json")))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(option);
				System.out.println("Firebase App has been initialized");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testFCMSendMessage() {
		FCMService service = new FCMService(antrianFCMDao);
		
		NotificationRequest request = new NotificationRequest();
		request.setTitle("Test FCM Message");
		request.setMessage("FCM Say...Hello World!!!");
		request.setTopic("foo");
		request.setToken("d7PDKnfFS327_0mjH7U9zg:APA91bHcMEoFD3EGFkRsGf2dSq6omuDIUNL719"
				+ "yOcxW1sobTYwts0yaZSi7Fdk9zwsYJdEE28FdmcFG5tCINF1p0xLnOEHDC_QwQD12I2xTS"
				+ "-qb8m-CbPPWuqflNLy1Wg9pA9xYhb1sI");
		
		try {
			service.sendMessageAndSaveToAntrian(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
