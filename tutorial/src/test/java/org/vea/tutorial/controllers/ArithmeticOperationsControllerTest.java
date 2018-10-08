package org.vea.tutorial.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ArithmeticOperationsController.class)
public class ArithmeticOperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void arithmeticOperationsView() throws Exception {

        mockMvc.perform(get("/arithmetic-operations"))
                .andExpect(view().name(equalTo("arithmetic-operations")));
    }

    @Test
    public void listInModel() throws Exception {
        mockMvc.perform(get("/arithmetic-operations"))
                .andExpect(model().attribute("prodStat", instanceOf(List.class)));
    }

    @Test
    public void modelContainsExecMode() throws Exception {
        mockMvc.perform(get("/arithmetic-operations"))
                .andExpect(model().attribute("execMode", allOf(
                        instanceOf(String.class),
                        equalTo("dev")
                        )
                ));
    }

    @Test
    public void testReturnViewName() {
        ArithmeticOperationsController controller = new ArithmeticOperationsController();
        Model mockModel = mock(Model.class);
        String viewName = controller.getArithmeticOperationsView(mockModel);

        Assert.assertThat(viewName, equalTo("arithmetic-operations"));
    }
}