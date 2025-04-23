package org.example.MavenProject;

import org.example.MavenProject.DBModel.Habit;
import org.example.MavenProject.DBModel.Users;
import org.example.MavenProject.DBRepository.HabitRepo;
import org.example.MavenProject.DBRepository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired private UsersRepo usersRepo;
    @Autowired private HabitRepo habitsRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String redirect(){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
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

    @PostMapping("/login")
    public String loginAttempt(@ModelAttribute AuthRequest authRequest){

       String currUsername = authRequest.getUsername();
        String currPassword = authRequest.getPassword();

        try{

          Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken((authRequest.getUsername()),(authRequest.getPassword())));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return "redirect:/dashboard";
        }
        catch (Exception e){
            //PRINT AN ERROR IF LOGIN FAILED
            return "loginPage";
        }
    }

    @GetMapping("/logout")
    public String logout(){

        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }

    @GetMapping("/createAccount")
    public String createAccount(){
        return "accountPage";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute Users user){
        List<Users> test = usersRepo.checkUser(user.getUsername());
        if(test.isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            usersRepo.save(user);
            return "redirect:/login";
        }
        else{
            //DISPLAY THAT THIS USERNAME EXISTS
            test.clear();
            return "accountPage";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m){


            List<Habit> habits = habitsRepo.getGoals(SecurityContextHolder.getContext().getAuthentication().getName());
            m.addAttribute("habits", habits);//habits added to model, used to pass data to thymeleaf in html file
            return "dashboardPage";

    }

    @GetMapping("/addGoal")
    public String addGoal(){

            return "addGoalPage";
    }


    @PostMapping("/addingGoal")
    @ResponseBody
    public String addingGoal(
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        String trimmedName = name.trim();

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Habit> existingHabit = habitsRepo.findByUsernameAndNameIgnoreCase(userName, trimmedName);


        if (existingHabit.isPresent()) {
            return "error: You already have a habit named '" + trimmedName + "'. Please choose a different name.";
        } else {

            // Create the new Habit object using the correct constructor
            Habit h = new Habit(userName, trimmedName, description);
            // Save the new habit to the database

            habitsRepo.save(h);
            return "success";
        }
    }

    @GetMapping("/removeGoal")
    public String removeGoal(Model m){



            List<Habit> habits = habitsRepo.getGoals(SecurityContextHolder.getContext().getAuthentication().getName());
            m.addAttribute("habits", habits);//habits added to model, used to pass data to thymeleaf in html file
            return "removeGoalPage";

    }
    @PostMapping("/removeGoal")
    public String removingGoal(@RequestParam String name){
        habitsRepo.deleteByName(SecurityContextHolder.getContext().getAuthentication().getName(), name);
        return "redirect:/dashboard";
    }

    @PostMapping("/updateGoalStatus")
    @ResponseBody
    public String updateGoalStatus(@RequestParam String name, @RequestParam boolean isCompleted) {

        habitsRepo.checkHabit(SecurityContextHolder.getContext().getAuthentication().getName(), name, isCompleted);
        return "success";
    }
}
