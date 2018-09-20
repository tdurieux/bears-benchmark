package br.com.patiolegal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.ExitRequestDTO;
import br.com.patiolegal.service.ExitService;

@RestController
public class ExitController {

	@Autowired
	private ExitService service;

	@PostMapping(value = "/api/v1/exit")
	public void exit(@Valid @RequestBody ExitRequestDTO request) {
		service.exit(request);
	}
}
