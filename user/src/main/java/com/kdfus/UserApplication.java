package com.kdfus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Cra2iTeT
 * @date 2022/7/16 10:46
 */
@SpringBootApplication
@EnableEurekaClient
//@EnableHystrix
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
