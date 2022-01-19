package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.service.AccountService;
import com.example.service.BalanceLoaderImpl;
import com.example.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ContactService contactService;
    private final BalanceLoaderImpl balanceLoader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountController(AccountService accountService, ContactService contactService, BalanceLoaderImpl balanceLoader, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountService = accountService;
        this.contactService = contactService;
        this.balanceLoader = balanceLoader;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/contacts")
    public ModelAndView index(Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/index");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Account account = accountService.getByEmail(userDetails.getUsername());
        model.addObject("contacts",contactService.getByAccountId(account.getId()));
        return model;
    }

    @GetMapping
    public ModelAndView show(Authentication authentication) {
        ModelAndView model = new ModelAndView("accounts/show");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Account account = accountService.getByEmail(userDetails.getUsername());
        model.addObject("account", account);
        Contact contact = contactService.get(account.getTelephoneNumber());
        model.addObject("contact", contact);
        model.addObject("balance", balanceLoader.getAccountBalance(account.getId()));
        return model;
    }

    @GetMapping("/new")
    public ModelAndView newAccount(@ModelAttribute("account") Account account) {
        ModelAndView model = new ModelAndView("accounts/new");
        model.addObject("param", null);
        return model;
    }

    @PostMapping("/create")
    public String create(@RequestParam("name") String name, @RequestParam("telephoneNumber") String telephoneNumber,
                         @RequestParam("email") String email, @RequestParam("encodedPassword") String encryptedPassword, Model model) {  //@ModelAttribute("account") Account account
        Account account;
        Contact contact;
        try {
            contact = new Contact(name, telephoneNumber);
            contactService.save(contact);

            account = new Account(email, telephoneNumber, bCryptPasswordEncoder.encode(encryptedPassword));
            account.setId(account.getId());
            accountService.save(account);
        } catch (ConstraintViolationException e) {
            String message = e.getMessage();
            model.addAttribute("text", message);
            return "accounts/new";
        }

        model.addAttribute("contact", contact);
        model.addAttribute("account", accountService.get(account.getId()));

        return "home";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("account", accountService.get(id));
        model.addAttribute("email", accountService.get(id).getEmail());
        return "accounts/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("account") @Valid Account account, BindingResult bindingResult,
                         @PathVariable("id") int id, @RequestParam("email") String email) {
        if (bindingResult.hasErrors())
            return "accounts/edit";

        accountService.update(id,email);
        return "redirect:/accounts";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        accountService.delete(id);
        return "home";
    }
}
