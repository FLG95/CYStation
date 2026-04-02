package io.squid.CyStation;

import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CyStationApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CyStationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Server started");

    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            repo.save(new User(null, "Alice", "A"));
            repo.save(new User(null, "Bob", "B"));

        };
    }
}

