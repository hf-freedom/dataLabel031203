package com.example.todo.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/todo")
    public String todo() {
        return "todo";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
