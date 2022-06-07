package com.topekox.pembayaran.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "Username Tidak Boleh Kosong")
	private String username;
	@NotBlank(message = "Password Tidak Boleh Kosong")
	private String password;
	
}
