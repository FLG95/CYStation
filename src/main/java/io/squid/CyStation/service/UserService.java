package io.squid.CyStation.service;

import io.squid.CyStation.config.SecurityConfig;
import io.squid.CyStation.enums.Role;
import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }


    public void save(User user) {

        if (!user.isAdult()) {
            // fait quelque chose
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.PASSENGER);
        userRepository.save(user);
    }

    @Transactional
    public void addExp(User user){
        user.setExperience(user.getExperience()+1);

        if(user.getExperience()<0){
            user.setRole(Role.PASSENGER);
        }
        else if(user.getExperience()>=5 && user.getExperience()<10){
            user.setRole(Role.SCIENTIST);
        }
        else if (user.getExperience()>=10) {
            user.setRole(Role.ADMIN);
        }

        userRepository.save(user);
    }


    public void resetExp(User user){
        user.setExperience(0);
        user.setRole(Role.PASSENGER);

        userRepository.save(user);
    }

}


