package org.vea.tutorial.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.thymeleaf.context.LazyContextVariable;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductListController.class)
public class ProductListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getViewNameTest() {
        ProductListController controller = new ProductListController();
        Model mockModel = mock(Model.class);
        String viewName = controller.getProductListView(mockModel);

        assertThat(viewName, equalTo("product/list"));
    }

    @Test
    public void getViewNameControllerTest() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(view().name("product/list"));
    }

    @Test
    public void modelTest() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(model().attribute("products", allOf(
                        instanceOf(LazyContextVariable.class),
                        hasProperty("value", allOf(
                                instanceOf(List.class),
                                hasSize(3)
                                )
                        )))
                );
    }
}