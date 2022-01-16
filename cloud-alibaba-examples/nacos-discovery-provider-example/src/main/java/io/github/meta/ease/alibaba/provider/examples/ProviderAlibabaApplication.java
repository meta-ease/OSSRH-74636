package io.github.meta.ease.alibaba.provider.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ProviderAlibabaApplication {

    public static void main(String[] args) {
        System.setProperty("spring.application.name", "alibaba-provider-examples");
        SpringApplication.run(ProviderAlibabaApplication.class, args);
    }
}