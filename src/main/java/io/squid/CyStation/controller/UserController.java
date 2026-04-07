package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import io.squid.CyStation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

@Controller
public class UserController {

    UserRepository userRepository;
    UserService userService;

    public UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        if(userRepository.findUserByEmail(user.getEmail()) != null) {

            return "redirect:/register?error";
        }
        else {
            userService.save(user);
            return "redirect:/userInfo";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "public/register";
    }


    @GetMapping("/login")
    public String login() {

        return "public/login";
    }


    @GetMapping("/userInfo")
    public String userInfo(Principal principal, Model model){

        User user = userRepository.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "public/userInfo";
    }

}