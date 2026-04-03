package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import io.squid.CyStation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        if(userRepository.findUserByEmail(user.getEmail()).equals(user)){
            return "redirect:/login";
        }else{
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
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "public/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user) {
        userService.login(user);
        return "redirect:/userInfo";
    }

    @GetMapping("/userInfo")
    public String userInfo(Principal principal, Model model){

        User user = userRepository.findUserByEmail(principal.getName());

        model.addAttribute("user", user);
        return "public/userInfo";
    }


}
