package org.apiitalhrbe;

import org.apiitalhrbe.configs.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ApiItalhrBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiItalhrBeApplication.class, args);
    }

}
