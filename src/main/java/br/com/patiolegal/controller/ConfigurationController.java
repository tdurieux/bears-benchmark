package br.com.patiolegal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.ConfigurationRequestDTO;
import br.com.patiolegal.service.ConfigurationService;

@RestController
public class ConfigurationController {

	@Autowired
	private ConfigurationService service;
	
	@PostMapping(value = "/api/v1/configuration/save")
	private void save(@RequestBody @Valid ConfigurationRequestDTO request){
		service.save(request);
	}
	
}
