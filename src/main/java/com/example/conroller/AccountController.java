package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AccountService;
import com.example.ss.Acc;
import com.example.ss.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountRepo accountRepository;

    @Autowired
    public AccountController(AccountService accountService, AccountRepo accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "account/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("account", accountService.get(id));
        return "account/show";
    }

    @GetMapping("/new")
    public String newAccount(@ModelAttribute("account") Acc account) {   //Model model
        return "account/new";
    }

    @PostMapping()
    public String create(@RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("email") String email, @RequestParam("encryptedPassword") String encryptedPassword, Model model) {  //@ModelAttribute("account") Account account
        //User user = new User(name, telephoneNumber, email);
        Acc account = new Acc(email, phoneNumber, bCryptPasswordEncoder.encode(encryptedPassword));
        //Set generated id
        account.setId(account.getId());
        accountRepository.save(account); //save to database
        //accountService.save(account);
        return "redirect:/accounts";
    }
}
