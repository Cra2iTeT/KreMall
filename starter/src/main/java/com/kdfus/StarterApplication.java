package com.kdfus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/5/2 21:40
 */

@SpringBootApplication
@EnableEurekaClient
//@EnableHystrix
public class StarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }
}
