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
    public Boolean getCompleted() {
        return isCompleted;
    }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
