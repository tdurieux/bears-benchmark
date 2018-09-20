package br.com.patiolegal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.PartDTO;
import br.com.patiolegal.service.PartService;

@RestController
public class PartController {

    @Autowired
    private PartService service;

    @GetMapping(path = "/api/v1/part")
    public List<PartDTO> findAll() {
        return service.findAll();
    }
    
}
