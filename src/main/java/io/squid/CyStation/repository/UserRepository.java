package io.squid.CyStation.repository;

import io.squid.CyStation.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    public Optional<User> findUserByEmail(String email);


}
