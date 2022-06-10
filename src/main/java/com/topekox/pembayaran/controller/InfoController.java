package com.topekox.pembayaran.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topekox.pembayaran.fcm.NotificationRequest;
import com.topekox.pembayaran.service.FCMService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class InfoController {
	
	private FCMService service;
	
	@GetMapping(value = "/testMessage")
	public Map<String, String> test() {
		service.initialize();	
		
		NotificationRequest request = new NotificationRequest();
		request.setTitle("Test FCM Message");
		request.setMessage("FCM Say...Hello World!!!");
		request.setTopic("foo");
		request.setToken("d7PDKnfFS327_0mjH7U9zg:APA91bHcMEoFD3EGFkRsGf2dSq6omuDIUNL719"
				+ "yOcxW1sobTYwts0yaZSi7Fdk9zwsYJdEE28FdmcFG5tCINF1p0xLnOEHDC_QwQD12I2xTS"
				+ "-qb8m-CbPPWuqflNLy1Wg9pA9xYhb1sI");
		
		service.messageToToken(request);
		
		return Map.of("message", "sukses");
	}

}
