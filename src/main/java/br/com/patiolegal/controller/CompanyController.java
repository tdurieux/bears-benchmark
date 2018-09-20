package br.com.patiolegal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.CompanyDTO;
import br.com.patiolegal.service.CompanyService;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService service;

    @GetMapping(value = "/api/v1/company/details/{id}")
    public @ResponseBody CompanyDTO getCompanyDetails(@PathVariable String id) {
        return service.findById(id);
    }

}
