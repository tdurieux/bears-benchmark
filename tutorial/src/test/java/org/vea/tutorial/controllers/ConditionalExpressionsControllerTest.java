package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.vea.tutorial.controllers.dto.User;
import org.vea.tutorial.services.UserService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest({ConditionalExpressionsController.class, UserService.class})
public class ConditionalExpressionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void viewName() {
        UserService mockService = mock(UserService.class);
        ConditionalExpressionsController controller = new ConditionalExpressionsController(mockService);

        Model mockModel = mock(Model.class);
        String viewName = controller.getView(mockModel);

        assertThat(viewName, equalTo("conditional-expressions"));

    }

    @Test
    public void viewNameMvcController() throws Exception {
        mockMvc.perform(get("/conditional-expressions"))
                .andExpect(view().name("conditional-expressions"));
    }

    @Test
    public void modelTest() throws Exception {

        mockMvc.perform(get("/conditional-expressions"))
                .andExpect(model().attribute("users", allOf(
                        instanceOf(List.class),
                        hasItem(instanceOf(User.class))
                )));

    }
}