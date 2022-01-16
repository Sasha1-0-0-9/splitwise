package com.example.conroller;

import com.example.common.AuthenticationSystem;
import com.example.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private AccountRepo accountRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Main page");
        if (!AuthenticationSystem.isLogged()) return "main";
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.getAuthentication();

        if(securityContext.getAuthentication().getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
            model.addAttribute("current_user", userDetails.getUsername());
            model.addAttribute("current_user_id", accountRepository
                    .getAccByName(userDetails.getUsername()).getId());
        }
        return "home";
    }
}
