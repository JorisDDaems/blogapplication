package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"","/","/index"})
    public String root(Principal principal, Model model) {
        if (principal == null){
            return "home";
        }
        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping({"/login"})
    public String login(Model model) {
        return "login";
    }

    //The below has the same function to what we implemented at root
    /*@GetMapping("/user")
    public String userIndex() {
        return "index";
    }
*/

    @RequestMapping("/404")
    public String notFoundError(){

        return "404";
    }



}
