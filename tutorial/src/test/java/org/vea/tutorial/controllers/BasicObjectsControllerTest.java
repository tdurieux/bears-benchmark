package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(BasicObjectsController.class)
public class BasicObjectsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void localeBasicObject() throws Exception {
        mockMvc.perform(get("/basic-objects"))
                .andExpect(content().string(containsString("Established locale language:")))
                .andExpect(content().string(containsString("en")));
    }

    @Test
    public void getRussianLocale() throws Exception {
        Locale locale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();


        mockMvc.perform(get("/basic-objects").locale(locale))
                .andExpect(content().string(containsString("Язык локали:")))
                .andExpect(content().string(containsString("ru")));
    }

    @Test
    public void calendarsObjectTest() throws Exception {
        mockMvc.perform(get("/basic-objects"))
                .andExpect(content().string(containsString("Today is: ")))
                .andExpect(model().attribute("today", instanceOf(Date.class)))
        ;
    }

    @Test
    public void viewNameTest(){
        BasicObjectsController controller = new BasicObjectsController();
        Model mockModel = mock(Model.class);
        String viewName = controller.getBasicObject(mockModel);

        assertThat(viewName, equalTo("basic-objects"));

    }
}