package org.vea.tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.Person;
import org.vea.tutorial.services.PersonService;

import java.util.Map;

@Controller
@RequestMapping("/variables")
public class VariablesController {

    private PersonService service;

    @Autowired
    public VariablesController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public String getVariables(Model model) {


        Person person = service.getPerson();
        model.addAttribute("person", person);


        Map<String, Person> personsByName = service.getPersonMap();
        model.addAttribute("personsByNameMap", personsByName);


        Person[] personArray = service.getPersonArray();
        model.addAttribute("personArray", personArray);


        return "variables";
    }
}
