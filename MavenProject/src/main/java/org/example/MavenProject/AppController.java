package org.example.MavenProject;

import org.example.MavenProject.DBModel.Habit;
import org.example.MavenProject.DBModel.Users;
import org.example.MavenProject.DBRepository.HabitRepo;
import org.example.MavenProject.DBRepository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    @Autowired private UsersRepo usersRepo;
    @Autowired private HabitRepo habitsRepo;
    private String currUsername;
    private String currPassword;
    private String currId;

    @GetMapping("/")
    public String redirect(){
        if(currUsername == null){
            return "redirect:/login";
        }
        else{
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @PostMapping("/loginAttempt")
    public String loginAttempt(@RequestParam String username, String password){
        currUsername = username;
        currPassword = password;
        List<Users> test = usersRepo.checkUserPass(username,password);
        if(!(test.isEmpty())){
            test.clear();
            return "redirect:/dashboard";
        }
        else{
            //PRINT AN ERROR IF LOGIN FAILED
            return "loginPage";
        }
    }

    @GetMapping("/logout")
    public String logout(){
        currUsername = null;
        currPassword = null;
        return "redirect:/login";
    }

    @GetMapping("/createAccount")
    public String createAccount(){
        return "accountPage";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute Users user){
        usersRepo.save(user);
        return "accountPage";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m){
        if(currUsername == null){
            return "redirect:/login";
        }
        else{
            //List<Habit> habits = habitsRepo.getGoals(currUsername);
            ArrayList<Habit> habits = new ArrayList<>();
            habits.add(new Habit("Gym", "lift weights"));
            habits.add(new Habit("HW", "Study"));
            habits.add(new Habit("HW", "Do Essay"));
            habits.add(new Habit("Protein", "drink protein shake"));

            m.addAttribute(habits);//habits added to model, used to pass data to thymeleaf in html file
            // this above will be prepopulated from database

            return "altDashboardPage";
        }
    }

    @GetMapping("/addGoal")
    public String addGoal(){
        if(currUsername == null){
            return "redirect:/login";
        }
        else {
            return "addGoalPage";
        }
    }

    @PostMapping("/addingGoal")
    public String addingGoal(@RequestParam String name, String description){
        Habit h = new Habit(currUsername, name, description);
        habitsRepo.save(h);
        return "redirect:/dashboard";
    }

    @GetMapping("/removeGoal")
    public String removeGoal(){
        if(currUsername == null){
            return "redirect:/login";
        }
        else {
            List<Habit> habits = habitsRepo.getGoals(currUsername);
            return "removeGoalPage";
        }
    }

}
