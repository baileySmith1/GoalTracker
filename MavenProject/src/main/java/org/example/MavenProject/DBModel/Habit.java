package org.example.MavenProject.DBModel;

public class Habit {

    String username;
    String name;
    String description;
    Boolean isCompleted = false;

    public Habit(String username, String name, String description){
        this.username = username;
        this.name = name;
        this.description = description;

    }
}
