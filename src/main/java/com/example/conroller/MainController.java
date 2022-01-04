package com.example.conroller;

import com.example.common.AuthenticationSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Main page");
        if (!AuthenticationSystem.isLogged()) return "main";
        return "home";
    }
}
