package br.com.patiolegal.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.Configuration;
import br.com.patiolegal.dto.ConfigurationRequestDTO;
import br.com.patiolegal.exception.ConfigurationNotFoundException;
import br.com.patiolegal.repository.ConfigurationRepository;

@Service
public class ConfigurationServiceBean implements ConfigurationService {

	private static final Logger LOG = LogManager.getLogger(ConfigurationServiceBean.class);

	@Autowired
	private ConfigurationRepository repository;

	@Override
	public void save(ConfigurationRequestDTO request) {
		LOG.info("Dados recebidos na requisição: " + request);

		Optional<Configuration> configurationOptional = repository.findByKey(request.getKey());

		if (configurationOptional.isPresent()) {
			Configuration configuration = configurationOptional.get();
			LOG.debug("Atualizando chave. Valor anterior: " + configuration.getValue());
			repository.save(configuration);
			LOG.debug("Chave atualizada.");
		} else {
			LOG.error("Configuração não localizada.");
			throw new ConfigurationNotFoundException();
		}

	}

}
