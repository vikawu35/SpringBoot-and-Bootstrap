package com.example.task_3_1_3.controllers;

import com.example.task_3_1_3.entities.Role;
import com.example.task_3_1_3.entities.User;
import com.example.task_3_1_3.services.RoleService;
import com.example.task_3_1_3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String printUsers(Model model, @ModelAttribute("newUser") User newUser, Principal principal) {
        User authenticatedUser = userService.findByUsername(principal.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("authenticatedUserRoles", authenticatedUser.getRoles());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("currentUser", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                          @ModelAttribute("currentUser") User currentUser,
                          @ModelAttribute("roles") List<Role> roles) {
        if (bindingResult.hasErrors()) {
            return "user-create";
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/user-delete/{id}")
    public String deleteUserFromTable(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PatchMapping("/user-update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //return "user-list";
            return "redirect:/admin";
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }
}