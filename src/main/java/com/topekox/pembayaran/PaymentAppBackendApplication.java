package com.topekox.pembayaran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentAppBackendApplication.class, args);
	}

}
