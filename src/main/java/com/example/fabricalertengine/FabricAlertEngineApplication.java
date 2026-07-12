package com.example.fabricalertengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FabricAlertEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(FabricAlertEngineApplication.class, args);
    }

}
