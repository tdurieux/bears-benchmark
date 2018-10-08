package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.User;

@Controller
@RequestMapping("/literals")
public class LiteralsController {
    @GetMapping
    public String getLiterals(Model model) {
        User user = User.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .nationality("American")
                .admin(true)
                .age(13)
                .build();

        model.addAttribute("user", user);
        return "literals";
    }
}
