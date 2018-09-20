package br.com.patiolegal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.patiolegal.domain.Protocol;

public interface ProtocolRepository extends MongoRepository<Protocol, String> {
	
	Optional<Protocol> findByProtocol(String protocol);
	
}
