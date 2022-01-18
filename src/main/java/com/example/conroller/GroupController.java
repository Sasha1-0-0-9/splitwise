package com.example.conroller;

import com.example.entity.*;
import com.example.service.ContactService;
import com.example.service.GroupService;
import com.example.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/accounts/{accountId}/groups")
public class GroupController {

    private final GroupService groupService;
    private final ContactService contactService;
    private final HelperService helperService;

    @Autowired
    public GroupController(GroupService groupService, ContactService contactService, HelperService helperService) {
        this.groupService = groupService;
        this.contactService = contactService;
        this.helperService = helperService;
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
        List<Contact> contacts = helperService.getContacts(id);
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
    public String addToGroup(@PathVariable("accountId") Integer accountId, Model model) {
        List<Contact> byAccountId = contactService.getByAccountId(accountId);
        model.addAttribute("contacts", byAccountId);
        return "groups/add";
    }

    @PostMapping("/{id}/add")
    public String add(@PathVariable("accountId") Integer accountId,
                      @PathVariable("id") Integer id, @RequestParam("contact") String contact,
                      Model model) {
        String text = helperService.temp(accountId, id, contact);
        if (text != null) {
            model.addAttribute("text", text);
        }

        model.addAttribute("group", groupService.get(id));
        List<Contact> contacts = helperService.getContacts(id);
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
