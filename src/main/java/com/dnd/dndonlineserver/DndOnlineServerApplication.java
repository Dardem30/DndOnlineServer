package com.dnd.dndonlineserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DndOnlineServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DndOnlineServerApplication.class, args);
    }

}
