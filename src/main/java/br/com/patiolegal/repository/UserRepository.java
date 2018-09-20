package br.com.patiolegal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.patiolegal.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    User findByUsername(String username);

}
