package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.vea.tutorial.controllers.dto.User;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(DefaultExpressionsController.class)
public class DefaultExpressionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void viewNameTest() {
        DefaultExpressionsController controller = new DefaultExpressionsController();

        Model mockModel = mock(Model.class);
        String viewName = controller.getViewName(mockModel);

        assertThat(viewName, equalTo("default-expressions"));
    }

    @Test
    public void viewNameControllerTest() throws Exception {

        mockMvc.perform(get("/default-expressions"))
                .andExpect(view().name("default-expressions"));
    }

    @Test
    public void modelUserControllerTest() throws Exception {
        mockMvc.perform(get("/default-expressions"))
                .andExpect(model().attribute("user",allOf(
                        instanceOf(User.class),
                        hasProperty("age",nullValue())
                )));
    }

}