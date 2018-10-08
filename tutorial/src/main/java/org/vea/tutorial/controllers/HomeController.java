package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public String home(Model model) {
        @SuppressWarnings("SpellCheckingInspection")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar instance = Calendar.getInstance();

        model.addAttribute("today", dateFormat.format(instance.getTime()));

        User user = User.builder()
                .firstName("Eugene")
                .lastName("Volkoedov")
                .nationality("Russian")
                .admin(true)
                .age(31)
                .build();

        model.addAttribute("user", user);

        model.addAttribute("welcomeMessageKey", "home.welcome");

        return "home";
    }

}
