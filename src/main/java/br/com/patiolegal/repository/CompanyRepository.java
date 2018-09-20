package br.com.patiolegal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.patiolegal.domain.Company;

public interface CompanyRepository extends MongoRepository<Company, String> {

}
