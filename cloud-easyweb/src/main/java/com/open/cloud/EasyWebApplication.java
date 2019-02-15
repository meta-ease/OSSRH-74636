package com.open.cloud;

import com.open.cloud.oauth.configuration.EnableOAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableOAuthClient
@EnableDiscoveryClient
@SpringBootApplication
public class EasyWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyWebApplication.class, args);
	}
}