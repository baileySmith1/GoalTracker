package org.example.MavenProject;

import org.example.MavenProject.DBModel.Users;
import org.example.MavenProject.DBRepository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    @Autowired private UsersRepo usersRepo;
    private String currUsername;
    private String currPassword;
    private String currId;

    @GetMapping("/")
    public String redirect(){
        return "redirect:/login";
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
            System.out.println(test);
            test.clear();
            return "redirect:/dashboard";
        }
        else{
            System.out.println(test);
            test.clear();
            return "loginPage";
        }
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
    public String dashboard(){
        return "dashboardPage";
    }

}
