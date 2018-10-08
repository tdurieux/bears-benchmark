package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.User;

@Controller
@RequestMapping("/default-expressions")
public class DefaultExpressionsController {
    @GetMapping
    public String getViewName(Model model) {
        User user = User.builder()
                .firstName("Ivan")
                .lastName("Ivanoff")
                .nationality("Ukraine")
                .admin(false)
                .age(null)
                .build();

        model.addAttribute("user", user);

        return "default-expressions";
    }
}
