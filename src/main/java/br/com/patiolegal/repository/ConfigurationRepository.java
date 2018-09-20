package br.com.patiolegal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.patiolegal.domain.Configuration;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String> {

	Optional<Configuration> findByKey(String key);

}
