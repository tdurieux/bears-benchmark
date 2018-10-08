package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/arithmetic-operations")
public class ArithmeticOperationsController {

    @GetMapping
    public String getArithmeticOperationsView(Model model) {

        List<String> prodStat = Arrays.asList("Hello", "World", "Ivan");
        model.addAttribute("prodStat", prodStat);

        model.addAttribute("execMode", "dev");

        return "arithmetic-operations";
    }
}
