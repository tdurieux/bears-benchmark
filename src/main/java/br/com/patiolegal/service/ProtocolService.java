package br.com.patiolegal.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.ProtocolRequestDTO;

public interface ProtocolService {

	FileIdentifierDTO generateProtocol(ProtocolRequestDTO request);

    ResponseEntity<InputStreamResource> downloadPdf(String protocol);
	
}
