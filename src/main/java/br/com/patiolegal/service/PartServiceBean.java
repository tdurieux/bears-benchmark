package br.com.patiolegal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.patiolegal.domain.Part;
import br.com.patiolegal.dto.PartDTO;
import br.com.patiolegal.dto.PartDTO.ArrestOrganBuilder;
import br.com.patiolegal.repository.PartRepository;

@Service
public class PartServiceBean implements PartService {
	
	@Autowired
	private PartRepository repository;
	
	@Override
	public List<PartDTO> findAll() { 
			List<Part> parts = repository.findAll();
			return parts.stream()
									.map(part -> new ArrestOrganBuilder()
															.withInitials(part.getInitials())
															.withDescription(part.getDescription())
															.build())
									.collect(Collectors.toList());
		
		
	}	
}
