package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.User;

@Controller
@RequestMapping("/user-info")
public class UserInfoController {

    @GetMapping
    public String getUserInfo(Model model) {
        User user= User.builder()
                .firstName("Ivan")
                .lastName("Ivanoff")
                .nationality("Russian")
                .admin(false)
                .age(15)
                .build();

        model.addAttribute("user", user);
        return "user-info";
    }
}
