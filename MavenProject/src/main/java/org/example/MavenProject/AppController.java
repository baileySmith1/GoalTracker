package org.example.MavenProject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AppController {

    @GetMapping("/")
    public String redirect(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboardPage";
    }

}
