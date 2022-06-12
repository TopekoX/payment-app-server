package com.topekox.pembayaran.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "Email Tidak Boleh Kosong")
	private String email;
	@NotBlank(message = "Password Tidak Boleh Kosong")
	private String password;
	
}
