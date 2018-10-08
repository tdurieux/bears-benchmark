package org.vea.tutorial.controllers;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.vea.tutorial.controllers.dto.User;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void modelTodayCheck() throws Exception {

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("today"))
                .andExpect(model().attribute("today", new IsInstanceOf(String.class)));

    }

    @Test
    public void modelUserNameCheck() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attribute("user", new IsInstanceOf(User.class)))
                .andExpect(model().attribute("user", hasProperty("firstName", equalTo("Eugene"))));
    }

    @Test
    public void homeContextCheck() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    public void modelWelcomeMessageKey() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attribute("welcomeMessageKey", new IsInstanceOf(String.class)))
                .andExpect(model().attribute("welcomeMessageKey", equalTo("home.welcome")));
    }

    @Test
    public void viewName() {
        HomeController controller = new HomeController();

        Model mockModel = mock(Model.class);
        String viewName = controller.home(mockModel);

        assertThat(viewName, equalTo("home"));
    }
}