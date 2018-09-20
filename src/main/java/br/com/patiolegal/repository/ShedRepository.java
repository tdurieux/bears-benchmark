package br.com.patiolegal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.patiolegal.domain.Shed;

public interface ShedRepository extends MongoRepository<Shed, String>{

	Optional<Shed> findByInitials(String initials);
	
}
