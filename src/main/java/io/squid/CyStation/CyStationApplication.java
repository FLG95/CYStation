package io.squid.CyStation;

import io.squid.CyStation.model.Co2Sensor;
import io.squid.CyStation.repository.SensorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CyStationApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CyStationApplication.class, args);
    }

    @Bean
    public CommandLineRunner testCreation(SensorRepository sensorRepository) {
        return args -> {
            Co2Sensor capteur = new Co2Sensor();
            capteur.setName("Détecteur Sas Principal");
            capteur.setPpmLevel(450);

            sensorRepository.save(capteur);

            System.out.println("Capteur sauvegardé dans MariaDB.");
        };
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Server started");
    }
}