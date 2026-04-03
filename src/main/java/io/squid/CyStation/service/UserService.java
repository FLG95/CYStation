package io.squid.CyStation.service;

import io.squid.CyStation.config.SecurityConfig;
import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }


    public User save(User user) {
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        return userRepository.save(user);
    }

    public User login(User user){

        List<User> userList = userRepository.findAll();

        for(int i=0; i< userList.size(); i++){
            if(user != userList.get(i)){
                return user;
            }
        }
        return null;
    }



}


