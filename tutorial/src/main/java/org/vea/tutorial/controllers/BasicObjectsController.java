package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/basic-objects")
public class BasicObjectsController {

    @GetMapping
    public String getBasicObject(Model model) {
        model.addAttribute("today", new Date());
        return "basic-objects";
    }
}
