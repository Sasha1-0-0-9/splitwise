package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.exception.ContactCreationException;
import com.example.service.AccountService;
import com.example.service.BalanceLoaderImpl;
import com.example.service.ContactService;
import com.example.ss.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/contacts")
    public String index(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("contacts", contactService.getByAccountId(id));
        return "accounts/index";
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("accounts", accountService.getAll());
        return "accounts/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        Account account = accountService.get(id);
        model.addAttribute("account", account);
        Contact contact = contactService.get(account.getTelephoneNumber());
        model.addAttribute("contact", contact);
        model.addAttribute("balance", balanceLoader.getAccountBalance(id));
        return "accounts/show";
    }

    @GetMapping("/new")
    public String newAccount(@ModelAttribute("account") Account account, Model model) {
        model.addAttribute("param", null);
        return "accounts/new";
    }

    @PostMapping()
    public String create(@RequestParam("name") @Valid String name, @RequestParam("telephoneNumber") String telephoneNumber,
                         @RequestParam("email") String email, @RequestParam("encryptedPassword") String encryptedPassword, Model model) {  //@ModelAttribute("account") Account account
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

        return "accounts/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", accountService.get(id));
        return "accounts/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Account account, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "accounts/edit";

        accountService.update(id, account);
        return "redirect:/accounts";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        accountService.delete(id);
        return "home";
    }
}
