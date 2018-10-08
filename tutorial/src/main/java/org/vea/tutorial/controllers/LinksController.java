package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.Order;

@Controller
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public String getViews(Model model) {
        Order order = Order.builder().id(3).build();
        model.addAttribute("order", order);
        return "links";
    }
}
