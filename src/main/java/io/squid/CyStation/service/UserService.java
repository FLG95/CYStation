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
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_PASSENGER);
        user.setEnable(false);
        userRepository.save(user);
    }

    @Transactional
    public boolean activateAccount(String token) {
        Optional<User> optUser = userRepository.findByActivationToken(token);

        if (optUser.isEmpty()) {
            return false;
        }

        User user = optUser.get();
        user.setEnable(true);
        user.setActivationToken(null);
        userRepository.save(user);

        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
        userRepository.delete(user);
    }

    @Transactional
    public void updateUserProfile(String email, User userForm) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setGender(userForm.getGender());
        user.setBirthDate(userForm.getBirthDate());
        user.setProfilePicture(userForm.getProfilePicture());

        userRepository.save(user);
    }

    @Transactional
    public void addExp(User user, int expToAdd) {
        user.setExperience(user.getExperience() + expToAdd);

        if (user.getExperience() <= 0) {
            user.setRole(Role.ROLE_PASSENGER);
            user.setExperience(0);
        } else if (user.getExperience() >= 50 && user.getExperience() < 100) {
            user.setRole(Role.ROLE_SCIENTIST);
        } else if (user.getExperience() >= 100 && user.getExperience() < 999) {
            user.setRole(Role.ROLE_ADMIN);
        } else if (user.getExperience() >= 999) {
            user.setExperience(999);
        }

        userRepository.save(user);
        refreshUserRole(user);
    }

    public void resetExp(User user) {
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

    @Transactional
    public int addExpAndLevelUp(Long id, int amount) {
        User user = userRepository.findById(id).orElseThrow();

        int totalExp = user.getExperience() + amount;
        if (totalExp > 999) totalExp = 999;
        if (totalExp < 0) totalExp = 0;
        user.setExperience(totalExp);

        if (totalExp >= 100) {
            user.setRole(Role.ROLE_ADMIN);
        } else if (totalExp >= 50) {
            user.setRole(Role.ROLE_SCIENTIST);
        } else {
            user.setRole(Role.ROLE_PASSENGER);
        }
        userRepository.save(user);

        return user.getExperience();
    }

    public String toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isEnable()) {
            String token = UUID.randomUUID().toString();
            user.setActivationToken(token);
            userRepository.save(user);
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName(), token);
            return "MAIL_SENT";
        } else {
            user.setEnable(false);
            user.setActivationToken(null);
            userRepository.save(user);
            return "DISABLED";
        }
    }
}