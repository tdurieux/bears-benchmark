package org.vea.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class DetailsController {
    @GetMapping("/details")
    public String getPlainDetails() {
        return "details";
    }

    @GetMapping(value = "/details", params = "orderId")
    public String getOrderDetailsRequestParameter(@RequestParam(name = "orderId") long orderId, Model model) {
        return getOrderDetails(orderId, model);

    }

    @RequestMapping("{orderId}/details")
    public String getOrderDetailsPathVariable(@PathVariable("orderId") long orderId, Model model) {
        return getOrderDetails(orderId, model);

    }

    private String getOrderDetails(Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order-details";
    }


}
