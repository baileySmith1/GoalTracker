package org.example.MavenProject.DBRepository;

import org.example.MavenProject.DBModel.Habit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import java.util.Optional;

import java.util.List;

public interface HabitRepo extends MongoRepository<Habit, String> {

    @Query("{username : ?0}")
    List<Habit> getGoals(String user);

    @Query(value = "{username : ?0, name: ?1}", delete = true)
    Habit deleteByName(String user, String name);

    @Query("{username : ?0, name: ?1}")
    @Update("{'$set' : {'isCompleted' : ?2}}")
    void checkHabit(String user, String name, Boolean value);

    Optional<Habit> findByUsernameAndNameIgnoreCase(String username, String name);
}

