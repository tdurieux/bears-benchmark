package br.com.patiolegal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.Shed;
import br.com.patiolegal.dto.ShedDTO;
import br.com.patiolegal.dto.ShedDTO.ShedBuilder;
import br.com.patiolegal.repository.ShedRepository;

@Service
public class ShedServiceBean implements ShedService {

	@Autowired
	private ShedRepository repository;

	@Override
	public List<ShedDTO> findAll() {

		List<Shed> sheds = repository.findAll();
		
		return sheds.stream()
				.map(shed -> new ShedBuilder()
						.withInitials(shed.getInitials())
						.withDescription(shed.getDescription())
						.build())
				.collect(Collectors.toList());
	}

}
