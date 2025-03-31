package org.example.MavenProject.DBRepository;

import org.example.MavenProject.DBModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface UsersRepo extends MongoRepository<Users, String> {

    @Query("{username : ?0, password : ?1}")
    List<Users> checkUserPass(String user, String pass);
}
