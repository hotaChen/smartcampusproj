package com.example.smartcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // 重定向到登录页面
        return "redirect:/auth/login.html";
    }
    
    @GetMapping("/index.html")
    public String indexHtml() {
        // 处理对index.html的直接访问，重定向到新的系统首页
        return "redirect:/system/index.html";
    }
    
    @GetMapping("/login")
    public String login() {
        // 为了兼容旧的登录路径，也重定向到新的登录页面
        return "redirect:/auth/login.html";
    }
    
    @GetMapping("/login.html")
    public String loginHtml() {
        // 处理对login.html的直接访问，重定向到新的登录页面
        return "redirect:/auth/login.html";
    }
    
    @GetMapping("/register")
    public String register() {
        // 为了兼容旧的注册路径，也重定向到新的注册页面
        return "redirect:/auth/register.html";
    }
    
    @GetMapping("/register.html")
    public String registerHtml() {
        // 处理对register.html的直接访问，重定向到新的注册页面
        return "redirect:/auth/register.html";
    }
}