package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("accounts", accountService.getAll());
        return "account/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("account", accountService.get(id));
        return "account/show";
    }

    @GetMapping("/new")
    public String newAccount(@ModelAttribute("account") Account account) {   //Model model
        return "account/new";
    }

    @PostMapping()
    public String create(@RequestParam("name") String name, @RequestParam("telephoneNumber") String telephoneNumber,
                         @RequestParam("email") String email, Model model) {  //@ModelAttribute("account") Account account
        User user = new User(name, telephoneNumber, email);
        Account account = new Account(user);
        accountService.save(account);
        return "account/success"; //"redirect:/accounts"
    }
}
