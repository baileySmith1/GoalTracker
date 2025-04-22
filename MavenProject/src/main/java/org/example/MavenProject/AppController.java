package org.example.MavenProject;

import org.example.MavenProject.DBModel.Habit;
import org.example.MavenProject.DBModel.Users;
import org.example.MavenProject.DBRepository.HabitRepo;
import org.example.MavenProject.DBRepository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.example.MavenProject.util.JWTUtil;

import java.util.Optional;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtil JWTUtil;
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

    @PostMapping("/login")
    public String loginAttempt(@RequestBody AuthRequest authRequest){

    authenticationManager.authenticate( new UsernamePasswordAuthenticationToken((authRequest.getUsername()),(authRequest.getPassword())));

        currUsername = authRequest.getUsername();
        currPassword = authRequest.getPassword();
        List<Users> test = usersRepo.checkUserPass(currUsername,currPassword);
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
        List<Users> test = usersRepo.checkUser(user.getUsername());
        if(test.isEmpty()){
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
        if(currUsername == null){
            return "redirect:/login";
        }
        else{
            List<Habit> habits = habitsRepo.getGoals(currUsername);
            m.addAttribute("habits", habits);//habits added to model, used to pass data to thymeleaf in html file
            return "dashboardPage";
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
    public String addingGoal(
            @RequestParam String name,
            @RequestParam(required = false) String description, // Make description optional
            RedirectAttributes redirectAttributes) {


        if(currUsername == null){

            redirectAttributes.addFlashAttribute("loginError", "Please log in to add a habit.");
            return "redirect:/login";
        }


        String trimmedName = name.trim();


        Optional<Habit> existingHabit = habitsRepo.findByUsernameAndNameIgnoreCase(currUsername, trimmedName);


        if (existingHabit.isPresent()) {
            // --- Duplicate Found ---
            // Add error message for the view
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You already have a habit named '" + trimmedName + "'. Please choose a different name.");
            // Add submitted values back for form repopulation
            redirectAttributes.addFlashAttribute("submittedName", trimmedName);
            redirectAttributes.addFlashAttribute("submittedDescription", description);
            // Redirect back to the add form
            return "redirect:/addGoal";

        } else {

            // Create the new Habit object using the correct constructor
            Habit h = new Habit(currUsername, trimmedName, description);
            // Save the new habit to the database
            habitsRepo.save(h);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Habit '" + trimmedName + "' added successfully!");
            // Redirect to the dashboard
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/removeGoal")
    public String removeGoal(Model m){
        if(currUsername == null){
            return "redirect:/login";
        }
        else {
            List<Habit> habits = habitsRepo.getGoals(currUsername);
            m.addAttribute("habits", habits);//habits added to model, used to pass data to thymeleaf in html file
            return "removeGoalPage";
        }
    }
    @PostMapping("/removeGoal")
    public String removingGoal(@RequestParam String name){
        habitsRepo.deleteByName(currUsername, name);
        return "redirect:/dashboard";
    }

    @PostMapping("/updateGoalStatus")
    @ResponseBody
    public String updateGoalStatus(@RequestParam String name, @RequestParam boolean isCompleted) {
        if (currUsername == null) {
            return "error: not logged in";
        }
        habitsRepo.checkHabit(currUsername, name, isCompleted);
        return "success";
    }
}
