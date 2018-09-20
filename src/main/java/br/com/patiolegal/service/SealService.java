package br.com.patiolegal.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.SealRequestDTO;

public interface SealService {

    FileIdentifierDTO generateSeal(SealRequestDTO request);

    ResponseEntity<InputStreamResource> downloadSeal(String id);

}
