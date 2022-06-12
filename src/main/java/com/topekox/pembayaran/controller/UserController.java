package com.topekox.pembayaran.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topekox.pembayaran.service.PembayaranService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private PembayaranService pembayaranService;
	
	@Autowired
	public UserController(PembayaranService pembayaranService) {
		this.pembayaranService = pembayaranService;
	}

	@PostMapping(value = "/token")
	public void updateToken(@RequestBody Map<String, String> data) {
		log.info("Request diterima: " + data);
		pembayaranService.updateToken(data.get("email").toString(),
				data.get("token").toString());		
	}

}
