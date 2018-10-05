package csse.users;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDAO extends MongoRepository<ApplicationUser, String>{

	ApplicationUser findByUsername(String username);
}
