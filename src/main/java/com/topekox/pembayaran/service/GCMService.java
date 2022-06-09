package com.topekox.pembayaran.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Google Cloud Messaging API (Legacy) / GCM adalah versi lama dari
 * Cloud Messaging Firebase untuk yang baru bernama Firebase Cloud Messaging API (V1) 
 * Read More: https://firebase.google.com/docs/cloud-messaging/migrate-v1
 * 
 * @author ucup
 */

@Service
public class GCMService {

	private static final String API_KEY = "AAAAoW3LRso:APA91bE-d30wUig-PCWIl5bYMQn_zosSEsyEVa2lL4k9XAUFo5RABOLMHg684MMwosrAWl9g3IAi3BG5hGpCDI2YFkmRP5FXvoDwaYCTIiSNl3bGHkDAMALsAE-EdORjoYQBm6ny7LbH";
	private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	private static final Logger LOGGER = LoggerFactory.getLogger(GCMService.class.getName());
	private static final String TAG = "GCM Service: ";
	
	private RestTemplate restTemplate = new RestTemplate();

	public GCMService() {
		restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor() {
			
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				request.getHeaders().add("Content-Type", "application/json");
				request.getHeaders().add("Authorization", "key=" + API_KEY);
				
				return execution.execute(request, body);
			}
		}));
	}
	
	public void kirimMessage(String tujuan, Map<String, Object> data) {
		Map<String, Object> gcmRequest = new HashMap<>();
		gcmRequest.put("to", tujuan);
		gcmRequest.put("notification", data);
		Map<String, Object> result = restTemplate.postForObject(FCM_URL, gcmRequest, Map.class);
		LOGGER.debug(TAG + "[{}]", result.get("success"));
	}
	
}
