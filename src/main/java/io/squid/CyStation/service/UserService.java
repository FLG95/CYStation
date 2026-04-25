package io.squid.CyStation.service;

import io.squid.CyStation.enums.Role;
import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        user.setRole(Role.ROLE_PASSENGER);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        if (user != null) {
            userRepository.delete(user);
        }
    }


    @Transactional
    public void updateUserProfile(String email, User userForm) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        if (user != null) {

            user.setFirstName(userForm.getFirstName());
            user.setLastName(userForm.getLastName());

            user.setProfilePicture(userForm.getProfilePicture());

            userRepository.save(user);
        }
    }

    @Transactional
    public void addExp(User user, int expToAdd){
        user.setExperience(user.getExperience()+expToAdd);

        if(user.getExperience()<= 0){
            user.setRole(Role.ROLE_PASSENGER);
        }
        else if(user.getExperience()>=50 && user.getExperience()<100){
            user.setRole(Role.ROLE_SCIENTIST);
        }
        else if (user.getExperience()>=100 && user.getExperience() < 999) {
            user.setRole(Role.ROLE_ADMIN);
        }else if (user.getExperience() >= 999){
            user.setExperience(999);
        }

        userRepository.save(user);
        refreshUserRole(user);

    }



    public void resetExp(User user){
        user.setExperience(0);
        user.setRole(Role.ROLE_PASSENGER);

        userRepository.save(user);
        refreshUserRole(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : utilisateur inexistant.");
        }
        userRepository.deleteById(id);
    }

    public void refreshUserRole(User user) {

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}




