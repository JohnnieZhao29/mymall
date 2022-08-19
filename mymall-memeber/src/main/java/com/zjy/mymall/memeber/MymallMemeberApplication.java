package com.zjy.mymall.memeber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.zjy.mymall.memeber.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MymallMemeberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MymallMemeberApplication.class, args);
    }

}
