package com.example.conroller;

import com.example.entity.*;
import com.example.exception.AccountNotFoundException;
import com.example.exception.ContactCreationException;
import com.example.service.AccountService;
import com.example.service.ContactService;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.entity.ExpenseType.USER;

@Controller
@RequestMapping("/accounts/{accountId}/groups")
public class GroupController {

    private final GroupService groupService;
    private final AccountService accountService;
    private final ContactService contactService;

    @Autowired
    public GroupController(GroupService groupService, AccountService accountService, ContactService contactService) {
        this.groupService = groupService;
        this.accountService = accountService;
        this.contactService = contactService;
    }

    @GetMapping()
    public String getAll(@PathVariable("accountId") Integer accountId, Model model) {
        List<Group> groups = groupService.getAllByAccountId(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groups", groups);
        return "groups/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("accountId") Integer accountId, @PathVariable("id") Integer id, Model model) {
        model.addAttribute("group", groupService.get(id));
        List<Integer> idList = groupService.getIdAccounts(id);
        List<Account> accounts = idList.stream()
                .map(s -> accountService.get(s))
                .collect(Collectors.toList());
        List<Contact> contacts = accounts.stream()
                .map(s -> contactService.get(s.getTelephoneNumber()))
                .collect(Collectors.toList());
        model.addAttribute("contacts", contacts);
        model.addAttribute("accountId", accountId);
        model.addAttribute("text", null);
        return "groups/show";
    }

    @GetMapping("/new")
    public String newGroup(@PathVariable("accountId") Integer accountId, Model model) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("text", null);
        return "groups/new";
    }

    @PostMapping("/new")
    public String createGroup(@RequestParam("name") String name,
                              @PathVariable("accountId") Integer accountId, Model model) {
        model.addAttribute("accountId", accountId);
        try {
            Group group = new Group(name, accountId);
            groupService.save(group);
        } catch (ConstraintViolationException e) {
            String message = e.getMessage();
            model.addAttribute("text", message);
            return "groups/new";
        }
        List<Group> groups = groupService.getAllByAccountId(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groups", groups);
        return "groups/index";
    }

    @DeleteMapping("/{id}")
    public String leave(@PathVariable("accountId") Integer accountId, @PathVariable("id") int id) {
        groupService.leaveGroup(id, accountId);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/add")
    public String addToGroup(@PathVariable("accountId") Integer accountId, @PathVariable("id") Integer id,
                             Model model) {
        List<Contact> byAccountId = contactService.getByAccountId(accountId);
        model.addAttribute("contacts", byAccountId);
        return "groups/add";
    }

    @PostMapping("/{id}/add")
    public String add(@PathVariable("accountId") Integer accountId,
                      @PathVariable("id") Integer id, @RequestParam("contact") String contact,
                      Model model) {

        List<Integer> idList = groupService.getIdAccounts(id);
        List<Account> accounts = idList.stream()
                .map(s -> accountService.get(s))
                .collect(Collectors.toList());
        List<Contact> groupContacts = accounts.stream()
                .map(s -> contactService.get(s.getTelephoneNumber()))
                .collect(Collectors.toList());

        Contact value = groupContacts.stream()
                .filter(p -> p.getTelephoneNumber().equals(contact))
                .findAny().orElse(null);

        if (value != null) {
            String message = "An account with this number is already in the group";
            model.addAttribute("text", message);
        } else {
            try {
                Account account = accountService.getByTelephoneNumber(contact);
                groupService.addToGroup(id, accountId, account.getId());
            } catch (AccountNotFoundException e) {
                String message = e.getMessage();
                model.addAttribute("text", message);
            }
        }

        model.addAttribute("group", groupService.get(id));
        idList = groupService.getIdAccounts(id);
        accounts = idList.stream()
                .map(s -> accountService.get(s))
                .collect(Collectors.toList());
        List<Contact> contacts = accounts.stream()
                .map(s -> contactService.get(s.getTelephoneNumber()))
                .collect(Collectors.toList());
        model.addAttribute("contacts", contacts);
        model.addAttribute("contacts", contacts);
        model.addAttribute("accountId", accountId);
        model.addAttribute("text", null);

        return "groups/show";
    }

    @GetMapping("/{id}/leave")
    public String leaveGroup(@PathVariable("accountId") Integer accountId,
                             @PathVariable("id") Integer id, Model model) {

        groupService.leaveGroup(id, accountId);

        List<Group> groups = groupService.getAllByAccountId(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groups", groups);
        return "groups/index";
    }
}
