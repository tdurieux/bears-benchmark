package br.com.patiolegal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.ShedDTO;
import br.com.patiolegal.service.ShedService;

@RestController
public class ShedController {

    @Autowired
    private ShedService service;

    @GetMapping(value = "/api/v1/shed")
    public @ResponseBody List<ShedDTO> getSheds() {
        return service.findAll();
    }

}
