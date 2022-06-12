package com.topekox.pembayaran;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class LogTest {
	
	@Test
	void testLoopingLogging() {
		for (int i = 0; i < 100_100; i++) {
			log.info("Hello World {}", i);
		}
	}

}
