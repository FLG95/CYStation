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
    public void deleteAccount(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user != null) {
            userRepository.delete(user);
        }
    }


    @Transactional
    public void updateUserProfile(String email, User updatedData) {
        User user = userRepository.findUserByEmail(email);

        if (user != null) {

            user.setFirstName(updatedData.getFirstName());
            user.setLastName(updatedData.getLastName());
            user.setBirthDate(updatedData.getBirthDate());
            user.setGender(updatedData.getGender());

            userRepository.save(user);
        }
    }

    @Transactional
    public void addExp(User user, int expToAdd){
        user.setExperience(user.getExperience()+expToAdd);

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


