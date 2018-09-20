package br.com.patiolegal.service;

import br.com.patiolegal.dto.CompanyDTO;

public interface CompanyService {

	CompanyDTO findById(String id);
	
}
