package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vea.tutorial.controllers.dto.Order;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(LinksController.class)
public class LinksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getViews() throws Exception {
        mockMvc.perform(get("/links"))
                .andExpect(content().string(containsString("Plain Details")));
    }

    @Test
    public void orderTest() throws Exception {
        mockMvc.perform(get("/links"))
                .andExpect(model().attribute("order",allOf(
                        instanceOf(Order.class),
                        hasProperty("id",equalTo(3L))
                        )
                ));
    }
}