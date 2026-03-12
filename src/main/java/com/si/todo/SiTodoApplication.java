package com.si.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class SiTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiTodoApplication.class, args);
    }

    public static int getRandomPort() {
        Random random = new Random();
        return 10000 + random.nextInt(1001);
    }
}
