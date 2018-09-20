package br.com.patiolegal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.ProtocolRequestDTO;
import br.com.patiolegal.service.ProtocolService;

@RestController
public class ProtocolController {

    @Autowired
    private ProtocolService service;
    
    @PostMapping(value = "/api/v1/print/protocol")
    public ResponseEntity<FileIdentifierDTO> generateProtocol(@RequestBody ProtocolRequestDTO request) {
        FileIdentifierDTO fileIdentifier = service.generateProtocol(request);
        return new ResponseEntity<>(fileIdentifier, HttpStatus.ACCEPTED);
    }
    
    @GetMapping(value = "/api/v1/print/protocol/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadProtocol(@PathVariable("id") String protocol) {
        return service.downloadPdf(protocol);
    }

}
