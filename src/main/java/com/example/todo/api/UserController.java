package com.example.todo.api;

import com.example.todo.entity.User;
import com.example.todo.todos.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户API控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("role", user.getRole());
            return ResponseEntity.ok(success(userInfo));
        }
        return ResponseEntity.badRequest().body(error("用户名或密码错误"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(success("退出成功"));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("role", user.getRole());
        return ResponseEntity.ok(success(userInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listUsers(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        if (!user.isAdmin()) {
            return ResponseEntity.status(403).body("无权限");
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(success(users));
    }

    @GetMapping("/list-normal")
    public ResponseEntity<?> listNormalUsers(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("未登录");
        }
        List<User> users = userService.getAllNormalUsers();
        return ResponseEntity.ok(success(users));
    }

    private Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
