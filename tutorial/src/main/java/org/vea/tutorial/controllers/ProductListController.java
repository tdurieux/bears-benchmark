package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vea.tutorial.controllers.dto.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductListController {

    @GetMapping("/list")
    public String getProductListView(Model model) {
        List<Product> productList = new ArrayList<>();

        Product product = Product.builder()
                .name("Cucumber")
                .price(BigDecimal.valueOf(5.00d))
                .stock(true)
                .build();
        productList.add(product);

        product = Product.builder()
                .from(product)
                .name("Carrot").build();
        productList.add(product);

        product = Product.builder()
                .from(product)
                .name("Cabbage")
                .price(BigDecimal.valueOf(3.15d))
                .build();
        productList.add(product);

        model.addAttribute("products", productList);
        return "product/list";
    }
}
