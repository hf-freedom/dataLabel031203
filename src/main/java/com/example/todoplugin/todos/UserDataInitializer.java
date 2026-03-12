package com.example.todoplugin.todos;

import com.example.todoplugin.map.SIUserCache;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class UserDataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("user.json");
        InputStream inputStream = resource.getInputStream();
        List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
        SIUserCache.initUsers(users);
        System.out.println("用户数据初始化完成，共加载 " + users.size() + " 个用户");
    }
}
