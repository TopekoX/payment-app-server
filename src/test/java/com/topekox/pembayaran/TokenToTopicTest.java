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
import com.topekox.pembayaran.service.FCMService;
import com.topekox.pembayaran.service.PembayaranService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TokenToTopicTest {

	@Autowired
	private FCMService service;
	@Autowired
	private PembayaranService pembayaranService;
	private static FirebaseOptions option;
	
	@BeforeAll
	public static void beforeAll() {
		try {
			option = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(new FileInputStream(
							"src/main/resources/my-firebase-with-spring-boot-firebase-adminsdk-pembayaran.json")))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(option);
				log.info("Firebase App has been initialized");
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@Test
	void testSendToken() {
		String token = "d7PDKnfFS327_0mjH7U9zg:APA91bFeYUCtunRoQsp9SDwPn_zAPwppQpSdFuhA5LtgyQLb3Yk9iF4SF"
				+ "c0YyP1JEm0Y1taAWfMuxvjrt0tQvvUVuUh8inK_BfdSz3qo7Y1EJbg9VSuVCqUBoaJLEyHysPDHehqKMsS-";
		String email = "ucup@gmail.com";
		
		
		try {
			log.info("Updating Token...");
			pembayaranService.updateToken(email, token);
			log.info("Token Updated...");
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

}
