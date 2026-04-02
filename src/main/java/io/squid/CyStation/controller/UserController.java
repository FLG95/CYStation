package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    List<User> getUser(){
        return userRepository.findAll();
    }

    @PostMapping(path="/users")
    List<User> addUser(@RequestBody User user){
        userRepository.save(user);
        return getUser();
    }

}
