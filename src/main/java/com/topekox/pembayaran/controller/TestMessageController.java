package com.topekox.pembayaran.controller;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topekox.pembayaran.fcm.NotificationRequest;
import com.topekox.pembayaran.service.FCMService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TestMessageController {
	
	private FCMService service;
	
	@PostMapping(value = "/testMessage")
	public Map<String, String> test(@RequestBody NotificationRequest notificationRequest) {
		
		NotificationRequest request = new NotificationRequest();
		request.setTitle(notificationRequest.getTitle());
		request.setMessage(notificationRequest.getMessage());
		request.setTopic(notificationRequest.getTopic());
		request.setToken(notificationRequest.getToken());
		
		try {
			service.sendMessageAndSaveToAntrian(request);
		} catch (Exception e) {
			return Map.of("message", e.getMessage());
		}		
		return Map.of("message", "sukses");
	}

}
