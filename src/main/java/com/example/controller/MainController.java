package com.example.controller;

import com.example.common.AuthenticationSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Main page");
        if (!AuthenticationSystem.isLogged()) return "main";
        return "home";
    }
}
