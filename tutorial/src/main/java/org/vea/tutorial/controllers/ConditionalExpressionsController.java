package org.vea.tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.User;
import org.vea.tutorial.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/conditional-expressions")
public class ConditionalExpressionsController {
    private UserService service;

    @Autowired
    public ConditionalExpressionsController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String getView(Model model) {
        List<User> users = service.getUsers();
        model.addAttribute("users", users);
        return "conditional-expressions";
    }
}
