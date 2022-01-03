package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.entity.Group;
import com.example.service.ContactService;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.entity.AccountRole.USER;

@Controller
@RequestMapping("/accounts/{accountId}/groups")
public class GroupController {

    private final GroupService groupService;
    private final ContactService contactService;

    @Autowired
    public GroupController(GroupService groupService, ContactService contactService) {
        this.groupService = groupService;
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
        List<Contact> groupContacts = groupService.getGroupContacts(id);
        model.addAttribute("contacts", groupService.getGroupContacts(id));
        model.addAttribute("accountId", accountId);
        String value = "true";
        model.addAttribute("param", null);
        return "groups/show";
    }

    @GetMapping("/new")
    public String newGroup(@ModelAttribute("group") Account account) {
        return "groups/new";
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

        List<Contact> groupContacts = groupService.getGroupContacts(id);

        Contact value = groupContacts.stream()
                .filter(p -> p.getTelephoneNumber().equals(contact))
                .findAny().orElse(null);

        if (value != null) {
            String name = "true";
            model.addAttribute("param", "true");
        } else {
            groupService.addToGroup(id, accountId, contactService.getAccount(contact).getId());
        }

        model.addAttribute("group", groupService.get(id));
        model.addAttribute("contacts", groupService.getGroupContacts(id));
        model.addAttribute("accountId", accountId);

        return "groups/show";
    }

    /*@GetMapping("/add")
    public String addAccount(@ModelAttribute("group") Account account) {
        return "groups/add";
    }

    @PostMapping("/{id}")
    public String create(@ModelAttribute("group") @Valid Group group,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "groups/new";

        accountService.save(account);
        return "redirect:/groups";
    }*/
}
