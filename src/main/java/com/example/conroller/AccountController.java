package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.exception.ContactCreationException;
import com.example.service.AccountService;
import com.example.service.BalanceLoaderImpl;
import com.example.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AccountController(AccountService accountService, ContactService contactService, BalanceLoaderImpl balanceLoader) {
        this.accountService = accountService;
        this.contactService = contactService;
        this.balanceLoader = balanceLoader;
    }

    @GetMapping("/{id}/contacts")
    public String index(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("contacts", contactService.getByAccountId(id));
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

    @GetMapping("/login")
    public String login(@ModelAttribute("account") Account account, Model model) {
        model.addAttribute("text", null);
        return "accounts/login";
    }

    @GetMapping("/new")
    public String newAccount(@ModelAttribute("account") Account account, Model model) {
        model.addAttribute("param", null);
        return "accounts/new";
    }

    @PostMapping("/login")
    public String entrance(@RequestParam("telephoneNumber") String telephoneNumber,
                           @RequestParam("encodedPassword") String encodedPassword,
                           ModelMap model) {

        Account account = accountService.getByTelephoneNumberAndPassword(telephoneNumber, encodedPassword);
        model.addAttribute("account", account);

        if (account == null) {
            String text = "An account with such a phone number and password was not found!";
            model.addAttribute("text", text);
            return "accounts/login";
        }

        Contact contact = contactService.get(telephoneNumber);
        model.addAttribute("contact", contact);
        model.addAttribute("balance", balanceLoader.getAccountBalance(account.getId()));
        return "accounts/show";
    }

    @PostMapping()
    public String create(@RequestParam("name") @Valid String name, @RequestParam("telephoneNumber") String telephoneNumber,
                         @RequestParam("email") String email, @RequestParam("encryptedPassword") String encryptedPassword, Model model) {  //@ModelAttribute("account") Account account
        Account account;
        Contact contact;
        int id;
        try {
            contact = new Contact(name, telephoneNumber, email);
            contactService.save(contact);

            account = new Account(telephoneNumber, encryptedPassword);
            id = accountService.save(account);
        } catch (ConstraintViolationException | ContactCreationException e) {
            String message = e.getMessage();
            model.addAttribute("text", message);
            return "accounts/new";
        }

        model.addAttribute("contact", contact);
        model.addAttribute("account", accountService.get(id));
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
