package br.com.patiolegal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.patiolegal.domain.Seal;

public interface SealRepository extends MongoRepository<Seal, String> {

}
