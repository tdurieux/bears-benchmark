package org.vea.tutorial.controllers;

import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.collection.IsArrayWithSize;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vea.tutorial.controllers.dto.Person;
import org.vea.tutorial.services.PersonService;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VariablesController.class, PersonService.class})
public class VariablesControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void addPersonVariable() throws Exception {
        String person = "person";
        mockMvc.perform(get("/variables"))
                .andExpect(model().attributeExists(person))
                .andExpect(model().attribute(person, new IsInstanceOf(Person.class)))
        ;

    }

    @Test
    public void addPersonByNameMapVariable() throws Exception {
        String personsByNameMap = "personsByNameMap";

        Person testPerson = Person.builder()
                .name("Ivan")
                .father("Ivan")
                .build();
        mockMvc.perform(get("/variables"))
                .andExpect(model().attributeExists(personsByNameMap))
                .andExpect(model().attribute(personsByNameMap, new IsInstanceOf(Map.class)))
                .andExpect(model().attribute(personsByNameMap, hasValue(new IsInstanceOf(Person.class))))
                .andExpect(model().attribute(personsByNameMap, hasValue(testPerson)))
                .andExpect(model().attribute(personsByNameMap, hasKey("Eugene")));


    }

    @Test
    public void viewName() throws Exception {
        mockMvc.perform(get("/variables"))
                .andExpect(view().name("variables"))
        ;
    }

    @Test
    public void personsArray() throws Exception {
        String personsArray = "personArray";

        Person person = Person.builder()
                .name("Ivan")
                .father("Ivan")
                .build();

        Person errorPerson = Person.builder()
                .name("Peter")
                .father("Ivan")
                .build();

        mockMvc.perform(get("/variables"))
                .andExpect(model().attribute(personsArray, new IsArrayWithSize<Person>(equalTo(2))))
                .andExpect(model().attribute(personsArray, new IsArrayContaining<>(equalTo(person))))
                .andExpect(model().attribute(personsArray, not(new IsArrayContaining<>(equalTo(errorPerson)))));

    }
}