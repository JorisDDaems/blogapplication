package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"user/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model, Principal principal){

        User existing = userService.findById(userId);
        if (existing == null || principal.getName() != existing.getEmail()) {
            return "redirect:/index";
        }

        model.addAttribute("user", userService.findById(userId));
        return "user/updateProfile";
    }

    @PostMapping({"user/{userId}/edit"})
    public String processUpdateProfile(@PathVariable Long userId, Principal principal, @ModelAttribute("user") User userForm){

        userService.updateProfile(userId, principal, userForm);
        return "redirect:/user/" + userId + "/profile";
    }



    @GetMapping("user/{userId}/profile")
    public String showProfile(@PathVariable Long userId, Model model, Principal principal) {

        User existing = userService.findById(userId);
        if (existing != null || principal.getName() == existing.getEmail()) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        }

        model.addAttribute("view", "user/profile");
        return "user/profile";
    }

}
