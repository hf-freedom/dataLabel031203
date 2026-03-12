package com.example.todoplugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class TodoPluginApplication {

    public static void main(String[] args) {
        int port = new Random().nextInt(1001) + 10000;
        System.setProperty("server.port", String.valueOf(port));
        SpringApplication.run(TodoPluginApplication.class, args);
        System.out.println("服务启动成功！访问地址：http://localhost:" + port);
    }

}
