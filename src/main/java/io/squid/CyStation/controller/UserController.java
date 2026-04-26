package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import io.squid.CyStation.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
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

        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register?error";
        }
        else {
            userService.save(user);
            return "redirect:/login";
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

        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        model.addAttribute("user", user);
        return "public/userInfo";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User userForm, Principal principal) {

        userService.updateUserProfile(principal.getName(), userForm);
        return "redirect:/userInfo?updated";
    }

    @PostMapping("/user/deleteProfile")
    public String deleteProfile(Principal principal, HttpServletRequest request) throws ServletException {

        userService.deleteAccount(principal.getName());
        request.logout();
        return "redirect:/login?deleted";
    }

    @PostMapping("/user/updateExp")
    public String updateExp(@RequestParam("amount") int amount, Principal principal, Model model){
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        userService.addExp(user, amount);
        model.addAttribute("user", user);
        return "redirect:/userInfo";
    }

    @PostMapping("/user/reset-exp")
    @ResponseBody
    public String resetExp(Principal principal, Model model){
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        userService.resetExp(user);
        model.addAttribute("user", user);
        return "redirect:/userInfo";
    }


    @GetMapping("/profiles")
    public String profiles(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("usersList", users);
        return "public/profiles";
    }

}