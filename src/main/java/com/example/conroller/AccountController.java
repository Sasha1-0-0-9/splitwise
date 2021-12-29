package com.example.conroller;

import com.example.entity.Account;
import com.example.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "accounts/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("account", accountService.get(id));
        return "accounts/show";
    }

    @GetMapping("/new")
    public String newAccount(@ModelAttribute("account") Account account) {
        return "accounts/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Account account,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "accounts/new";

        accountService.save(account);
        return "redirect:/accounts";
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

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        accountService.delete(id);
        return "redirect:/accounts";
    }
}
