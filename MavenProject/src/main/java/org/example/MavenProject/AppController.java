package org.example.MavenProject;

import org.example.MavenProject.DBModel.Users;
import org.example.MavenProject.DBRepository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String redirect(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @GetMapping("/createAccount")
    public String createAccount(){
        return "accountPage";
    }
    @PostMapping("/register")
    public String registerAccount(@ModelAttribute Users user){
        System.out.println(user.toString());
        return "accountPage";
    }

     @GetMapping("/dashboard")//whenever /dashboard html file visited this method runs
    public String dashboard(Model m){

        ArrayList<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Gym", "lift weights"));
        habits.add(new Habit("HW", "Study"));
        habits.add(new Habit("HW", "Do Essay"));
        habits.add(new Habit("Protein", "drink protein shake"));

        m.addAttribute(habits);//habits added to model, used to pass data to thymeleaf in html file
        // this above will be prepopulated from database


        return "dashboardPage";
    }


}
