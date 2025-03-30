package org.example.MavenProject.DBRepository;

import org.example.MavenProject.DBModel.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<Users, Integer> {

}
