package com.example.conroller;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.entity.Group;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accounts/{accountId}/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String getAll(@PathVariable("accountId") Integer accountId, Model model) {
        List<Group> groups = groupService.getAllByAccountId(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groups", groups);
        return "groups/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("group", groupService.get(id));
        List<Contact> groupContacts = groupService.getGroupContacts(id);
        model.addAttribute("contactMap", groupService.getGroupContacts(id));
        return "groups/show";
    }

    @GetMapping("/new")
    public String newGroup(@ModelAttribute("group") Account account) {
        return "groups/new";
    }

    /*@PostMapping()
    public String create(@ModelAttribute("group") @Valid Group group,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "groups/new";

        accountService.save(account);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("account", groupService.get(id));
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") @Valid Group group, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "groups/edit";

        groupService.update(id, account);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String leave(@PathVariable("accountId") Integer accountId, @PathVariable("id") int id) {
        groupService.leaveGroup(id, accountId);
        return "redirect:/groups";
    }*/

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
