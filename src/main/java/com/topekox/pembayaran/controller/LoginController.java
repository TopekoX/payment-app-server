package com.topekox.pembayaran.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topekox.pembayaran.dto.LoginRequest;

@RestController
@RequestMapping(value = "/api")
public class LoginController {

	@PostMapping(value = "/login")
	public Map<String, Object> login(@RequestBody @Valid LoginRequest loginRequest) {
		String username = "ucup";
		String password = "123456";
		
		Map<String, Object> result = new HashMap<>();
		
		if (username.equals(loginRequest.getUsername()) && password.equals(loginRequest.getPassword())) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}
			
		return result;
	}
	
}
