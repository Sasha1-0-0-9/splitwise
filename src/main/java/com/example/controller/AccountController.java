package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.service.AccountService;
import com.example.service.BalanceLoader;
import com.example.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ContactService contactService;
    private final BalanceLoader balanceLoader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountController(AccountService accountService, ContactService contactService, BalanceLoader balanceLoader, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountService = accountService;
        this.contactService = contactService;
        this.balanceLoader = balanceLoader;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/contacts")
    public ModelAndView index(Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/index");
        Account account = accountService.getByEmail(authentication.getName());
        model.addObject("contacts", contactService.getByAccountId(account.getId()));
        return model;
    }

    @GetMapping
    public ModelAndView show(Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/show");
        Account account = accountService.getByEmail(authentication.getName());
        model.addObject("account", account);
        model.addObject("contact", contactService.get(account.getTelephoneNumber()));
        model.addObject("balance", balanceLoader.getAccountBalance(account.getId()));
        return model;
    }

    @GetMapping("/new")
    public String newAccount() {
        return "accounts/new";
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam("name") String name, @RequestParam("telephoneNumber") String telephoneNumber,
                               @RequestParam("email") String email, @RequestParam("encodedPassword") String encryptedPassword) {
        ModelAndView model = new ModelAndView("home");
        Account account = new Account(email, telephoneNumber, bCryptPasswordEncoder.encode(encryptedPassword));
        accountService.save(account, name);
        return model;
    }

    @GetMapping("/edit")
    public ModelAndView edit(Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/edit");
        model.addObject("account", accountService.getByEmail(authentication.getName()));
        return model;
    }

    @PostMapping
    public String update(@RequestParam("email") String email, Authentication authentication) {
        accountService.update(authentication.getName(), email);
        return "main";
    }

    @GetMapping("/delete")
    public String delete(Authentication authentication) {
        accountService.delete(authentication.getName());
        return "main";
    }

    @GetMapping("/found")
    public String found() {
        return "accounts/found";
    }

    @PostMapping("/contact")
    public ModelAndView contact(@RequestParam("phoneNumber") String phoneNumber) {
        ModelAndView model = new ModelAndView("accounts/add");
        Contact contact = contactService.get(phoneNumber);
        model.addObject("contact", contact);
        return model;
    }

    @GetMapping("/add/{phoneNumber}")
    public ModelAndView found(@PathVariable("phoneNumber") String phoneNumber, Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/show");
        Account account = accountService.getByEmail(authentication.getName());
        contactService.add(account, phoneNumber);
        model.addObject("account", account);
        model.addObject("contact", contactService.get(account.getTelephoneNumber()));
        model.addObject("balance", balanceLoader.getAccountBalance(account.getId()));
        return model;
    }
}
