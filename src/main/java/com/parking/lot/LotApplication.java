package com.parking.lot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class LotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotApplication.class, args);
	}

}
