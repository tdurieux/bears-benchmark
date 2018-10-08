package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(DetailsController.class)
public class DetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPlainDetails() throws Exception {
        mockMvc.perform(get("/order/details")).andExpect(content().string(containsString("Plain Details")));
    }

    @Test
    public void getOrderDetailsRequestParameter() throws Exception {
        mockMvc.perform(get("/order/details?orderId=3")).andExpect(content().string(allOf(
                containsString("Order id = "),
                containsString("3")

        )));
    }

    @Test
    public void getOrderDetailsPathVariable() throws Exception {
        mockMvc.perform(get("/order/3/details")).andExpect(content().string(allOf(
                containsString("Order id = "),
                containsString("3")

        )));
    }
}