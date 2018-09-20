package br.com.patiolegal.service;

import java.util.List;

import br.com.patiolegal.dto.ProtocolDTO;
import br.com.patiolegal.dto.ProtocolRequestDTO;
import br.com.patiolegal.dto.SearchEntranceRequestDTO;
import br.com.patiolegal.dto.SearchEntranceResponseDTO;

public interface EntranceService {

	String save(ProtocolRequestDTO request);

	List<ProtocolDTO> find();

	List<SearchEntranceResponseDTO> search(SearchEntranceRequestDTO request);

}
