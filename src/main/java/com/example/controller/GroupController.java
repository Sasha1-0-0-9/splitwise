package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Balance;
import com.example.entity.Contact;
import com.example.entity.Group;
import com.example.service.AccountService;
import com.example.service.BalanceLoader;
import com.example.service.ContactService;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/accounts/groups")
public class GroupController {

    private final GroupService groupService;
    private final ContactService contactService;
    private final AccountService accountService;
    private final BalanceLoader balanceLoader;

    @Autowired
    public GroupController(GroupService groupService, ContactService contactService, AccountService accountService, BalanceLoader balanceLoader) {
        this.groupService = groupService;
        this.contactService = contactService;
        this.accountService = accountService;
        this.balanceLoader = balanceLoader;
    }

    @GetMapping()
    public ModelAndView getAll(Authentication authentication) {
        ModelAndView model = new ModelAndView("groups/index");
        Account account = accountService.getByEmail(authentication.getName());
        model.addObject("groups", groupService.getAllByAccountId(account.getId()));
        return model;
    }

    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") Integer id) {
        ModelAndView model = new ModelAndView("groups/show");
        model.addObject("group", groupService.get(id));
        String creator = accountService.get(groupService.get(id).getCreatorId()).getTelephoneNumber();
        model.addObject("creator", contactService.get(creator));
        List<Contact> contacts = groupService.getContacts(id);
        model.addObject("contacts", contacts);
        Map<Contact, Balance> balances = contacts.stream()
                .collect(Collectors.toMap(s -> s, s -> balanceLoader.getAccountBalance(id, accountService.getByTelephoneNumber(s.getPhoneNumber()).getId())));
        model.addObject("balances", balances);
        return model;
    }

    @GetMapping("/new")
    public String newGroup() {
        return "groups/new";
    }

    @PostMapping("/new")
    public ModelAndView createGroup(@RequestParam("name") String name, Authentication authentication) {
        ModelAndView model = new ModelAndView("groups/index");
        Account account = accountService.getByEmail(authentication.getName());
        Group group = new Group(name, account.getId());
        groupService.save(group);
        List<Group> groups = groupService.getAllByAccountId(account.getId());
        model.addObject("groups", groups);
        return model;
    }

    @DeleteMapping("/{id}")
    public String leave(@PathVariable("id") int id, Authentication authentication) {
        Account account = accountService.getByEmail(authentication.getName());
        groupService.leaveGroup(id, account.getId());
        return "home";
    }

    @GetMapping("/{id}/add")
    public ModelAndView addToGroup(@PathVariable("id") int id, Authentication authentication) {
        ModelAndView model = new ModelAndView("groups/add");
        Account account = accountService.getByEmail(authentication.getName());
        List<Contact> byAccountId = contactService.getByAccountId(account.getId());
        model.addObject("contacts", byAccountId);
        model.addObject("groupId", id);
        return model;
    }

    @PostMapping("/{id}/add")
    public ModelAndView add(@PathVariable("id") Integer id, @RequestParam("contact") String contact,
                            Authentication authentication) {
        ModelAndView model = new ModelAndView("groups/show");
        Account account = accountService.getByEmail(authentication.getName());
        groupService.addToGroup(account.getId(), id, contact);
        model.addObject("group", groupService.get(id));
        List<Contact> contacts = groupService.getContacts(id);
        model.addObject("contacts", contacts);
        return model;
    }

    @GetMapping("/{id}/leave")
    public ModelAndView leaveGroup(@PathVariable("id") Integer id, Authentication authentication) {
        ModelAndView model = new ModelAndView("groups/index");
        Account account = accountService.getByEmail(authentication.getName());
        groupService.leaveGroup(id, account.getId());
        List<Group> groups = groupService.getAllByAccountId(account.getId());
        model.addObject("groups", groups);
        return model;
    }
}
