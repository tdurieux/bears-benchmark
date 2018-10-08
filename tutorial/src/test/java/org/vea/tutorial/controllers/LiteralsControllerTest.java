package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vea.tutorial.controllers.dto.User;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(LiteralsController.class)
public class LiteralsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTextLiteral() throws Exception {
        mockMvc.perform(get("/literals"))
                .andExpect(content().string(allOf(
                        containsString("Now you are looking at a"),
                        containsString("working web application")
                        )

                ));
    }

    @Test
    public void getNumberLiteral() throws Exception {
        mockMvc.perform(get("/literals"))
                .andExpect(content().string(allOf(
                        containsString("The year is "),
                        containsString("2013"),
                        containsString("In two years, it will be"),
                        containsString("2015")
                        )

                ));
    }

    @Test
    public void checkModel() throws Exception {
        mockMvc.perform(get("/literals"))
                .andExpect(model().attribute("user", allOf(
                        instanceOf(User.class),
                        hasProperty("admin", equalTo(true)),
                        hasProperty("lastName", equalTo("Petrov"))

                )));
    }

    @Test
    public void userAdminNotShowInfo() throws Exception {
        mockMvc.perform(get("/literals"))
                .andExpect(content().string(not(containsString("User not admin"))));
    }

    @Test
    public void userAdminShowInfo() throws Exception {
        mockMvc.perform(get("/literals"))
                .andExpect(content().string(containsString("User admin")));

    }
}