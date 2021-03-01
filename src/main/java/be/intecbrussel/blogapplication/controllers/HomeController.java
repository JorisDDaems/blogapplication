package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class HomeController implements ErrorController {

    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @RequestMapping({"","/", "/home", "/index"})
    public String root(Principal principal, Model model) {

        model.addAttribute("posts", postService.getTenPosts());

        if (principal == null){
            return "home";
        }git checkout -b
        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping({"/user/{userId}/frontpage"})
    public String showLoggedinUserFrontPage(@PathVariable Long userId, Model model) {

        //model.addAttribute("view", "user/frontpage");
        model.addAttribute("user", userService.findById(userId));
        return "/user/frontpage";
    }


    @GetMapping({"/login", "/frontpage/login"})
    public String login(Model model) {
        return "login";
    }

    @GetMapping({"/logout"})
    public String logout(Model model) {
        return "home";
    }

    @RequestMapping("/404")
    public String notFoundError(){

        return "404";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
