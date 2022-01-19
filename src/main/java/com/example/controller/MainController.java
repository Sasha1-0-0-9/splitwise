package com.example.controller;

import com.example.common.AuthenticationSystem;
import com.example.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private AccountRepo accountRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Main page");
        if (!AuthenticationSystem.isLogged()) return "main";
        return "home";
    }
}
