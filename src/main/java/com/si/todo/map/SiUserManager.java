package com.si.todo.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class SiUserManager {

    private final Map<String, UserInfo> userMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("user.json");
            InputStream inputStream = resource.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String content = sb.toString();
            
            JSONArray jsonArray = JSON.parseArray(content);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                UserInfo user = new UserInfo();
                user.setUserId(obj.getString("userId"));
                user.setUsername(obj.getString("username"));
                user.setAdmin(obj.getBooleanValue("isAdmin"));
                userMap.put(user.getUserId(), user);
            }
        } catch (IOException e) {
            initDefaultUsers();
        }
    }

    private void initDefaultUsers() {
        UserInfo admin = new UserInfo();
        admin.setUserId("admin");
        admin.setUsername("管理员");
        admin.setAdmin(true);
        userMap.put("admin", admin);

        UserInfo user1 = new UserInfo();
        user1.setUserId("user001");
        user1.setUsername("张三");
        user1.setAdmin(false);
        userMap.put("user001", user1);

        UserInfo user2 = new UserInfo();
        user2.setUserId("user002");
        user2.setUsername("李四");
        user2.setAdmin(false);
        userMap.put("user002", user2);
    }

    public UserInfo getUser(String userId) {
        return userMap.get(userId);
    }

    public List<String> getAllUserIds() {
        return new ArrayList<>(userMap.keySet());
    }

    public List<UserInfo> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public boolean isAdmin(String userId) {
        UserInfo user = userMap.get(userId);
        return user != null && user.isAdmin();
    }

    public static class UserInfo {
        private String userId;
        private String username;
        private boolean isAdmin;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }
    }
}
