package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vea.tutorial.controllers.dto.User;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(UserInfoController.class)
public class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserInfo() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(model().attribute("user", allOf(
                        instanceOf(User.class)
                        , hasProperty("firstName", equalTo("Ivan"))
                        , hasProperty("lastName", equalTo("Ivanoff"))
                        , hasProperty("nationality", equalTo("Russian"))
                        ))
                );
    }

    @Test
    public void enLocaleContent() throws Exception {
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();

        mockMvc.perform(get("/user-info").locale(locale))
                .andExpect(content().string(allOf(
                        containsString("First name:")
                        ,containsString("Surname:")
                        ,containsString("Nationality:")
                )));
    }
}