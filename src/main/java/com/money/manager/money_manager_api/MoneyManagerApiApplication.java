package com.money.manager.money_manager_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyManagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyManagerApiApplication.class, args);
	}

}
