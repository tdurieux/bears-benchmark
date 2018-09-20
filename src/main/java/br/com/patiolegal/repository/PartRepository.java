package br.com.patiolegal.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.patiolegal.domain.Part;

@Repository
public interface PartRepository extends MongoRepository<Part, String> {

	Optional<Part> findByInitials(String initials);

}
