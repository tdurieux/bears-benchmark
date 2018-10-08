package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(SubscribeController.class)
public class SubscribeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void viewName() {
        SubscribeController controller = new SubscribeController();

        String viewName = controller.getView();

        assertThat(viewName, equalTo("subscribe"));
    }

    @Test
    public void viewNameControllerTest() throws Exception {

        mockMvc.perform(get("/subscribe"))
                .andExpect(view().name("subscribe"));

    }

}