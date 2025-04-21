package org.example.MavenProject.DBRepository;

import org.example.MavenProject.DBModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UsersRepo extends MongoRepository<Users, String> {

    @Query("{username : ?0, password : ?1}")
    List<Users> checkUserPass(String user, String pass);

    @Query("{username : ?0}")
    List<Users> checkUser(String user);

    @Query("{username : ?0}")
    Optional<Users> lookupUser(String username);

}
