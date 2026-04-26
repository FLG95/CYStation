package io.squid.CyStation.config;

import ch.qos.logback.core.status.Status;
import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.enums.Gender;
import io.squid.CyStation.enums.Role;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.User;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.UserRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
class DataInitializerConfiguration {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   ZoneRepository zoneRepository,
                                   DeviceService deviceService,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            createUserIfNotFound(userRepository, passwordEncoder,
                    "admin@cystation.fr", "Commandant", "Squid", "1234",
                    Role.ROLE_ADMIN, 999, LocalDate.of(1985, 5, 15), Gender.MAN);

            createUserIfNotFound(userRepository, passwordEncoder,
                    "Senku.Ichigami@cystation.fr", "Senku", "Ichigami", "1234",
                    Role.ROLE_SCIENTIST, 50, LocalDate.of(2004, 1, 4), Gender.MAN);

            createUserIfNotFound(userRepository, passwordEncoder,
                    "Jean.Dupond@cystation.fr", "Jean", "Dupond", "1234",
                    Role.ROLE_PASSENGER, 0, LocalDate.of(1998, 12, 20), Gender.OTHER);



            Zone bioLab = createZoneIfNotFound(zoneRepository, "Laboratoire Biologique",
                    "Recherche sur la survie végétale en milieu hostile.", "");

            Zone engineRoom = createZoneIfNotFound(zoneRepository, "Salle des Moteurs",
                    "Propulsion ionique et gestion de l'énergie.", "");


            if (bioLab.getDevices() == null || bioLab.getDevices().isEmpty()) {
                Device co2 = DeviceCategory.CO2_SENSOR.createInstance();
                co2.setName("Capteur Alpha");
                deviceService.addDeviceToZone(co2, bioLab.getId());
            }

            if (engineRoom.getDevices() == null || engineRoom.getDevices().isEmpty()) {
                Device gen = DeviceCategory.GENERATOR.createInstance();
                gen.setName("Generateur Principal");
                deviceService.addDeviceToZone(gen, engineRoom.getId());
            }


            System.out.println(">> Initialisation terminée !");
        };
    }

    private void createUserIfNotFound(UserRepository repo, PasswordEncoder encoder,
                                      String email, String fName, String lName,
                                      String pass, Role role, int exp,
                                      LocalDate birthDate, Gender gender) {
        if (repo.findUserByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setFirstName(fName);
            user.setLastName(lName);
            user.setPassword(encoder.encode(pass));
            user.setRole(role);
            user.setExperience(exp);
            user.setBirthDate(birthDate);
            user.setGender(gender);

            repo.saveAndFlush(user);
            System.out.println("Utilisateur spatial créé : " + email);
        }
    }


    private Zone createZoneIfNotFound(ZoneRepository repo, String name, String desc, String imgUrl) {
        Zone zone = repo.findZoneByName(name);

        if (zone == null) {
            zone = new Zone();
            zone.setName(name);
            zone.setDescription(desc);
            zone.setImageUrl(imgUrl);
            zone = repo.save(zone);
            System.out.println("Zone créée : " + name);
        }

        return zone;
    }

}