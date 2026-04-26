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
                    "Recherche sur la survie végétale en milieu hostile.", "https://cdna.artstation.com/p/assets/images/images/076/706/258/large/mael-martin-ldv-biolab-shot-01-copy.jpg?1717597539");

            Zone engineRoom = createZoneIfNotFound(zoneRepository, "Salle des Moteurs",
                    "Propulsion ionique et gestion de l'énergie.", "https://unknownworlds.com/_next/image?url=https%3A%2F%2Fd17c72h1ypygg7.cloudfront.net%2Fsubnautica%2Ffiles%2F2014%2F08%2FCyclops_EngineRoom_spinParts.jpg%2FCyclops_Engine_Room_spin_Parts_dc3988525c.jpg&w=1920&q=75");

            Zone reactorRoom = createZoneIfNotFound(zoneRepository, "Salle du Réacteur",
                    "Génération d'energie pour alimenter tout les systèmes de la station.", "https://cdna.artstation.com/p/assets/images/images/062/160/990/large/fredrik-giro-machine-councilroom.jpg?1682495988");

            Zone communicationRoom = createZoneIfNotFound(zoneRepository, "Salle des Communications",
                    "Gères toutes les communications et signaux entrants et sortant de la station", "https://media.craiyon.com/2025-07-15/-_kTZZzeRjOLHh1rPuhiHg.webp");

            Zone oxygenGeneratorRoom = createZoneIfNotFound(zoneRepository, "Salle du Générateur d'Oxygène",
                    "Génères l'oxygène requis à la survie de l'équipage", "https://cdna.artstation.com/p/assets/images/images/021/686/854/large/krista-hilge-render-1.jpg?1572574090");

            if (bioLab.getDevices() == null || bioLab.getDevices().isEmpty()) {
                Device cafe = DeviceCategory.CAFE.createInstance();
                Device radioactivity = DeviceCategory.RADIATION_SENSOR.createInstance();
                cafe.setName("Machine à Café");
                radioactivity.setName("Radiation de l'échantillon");
                deviceService.addDeviceToZone(cafe, bioLab.getId());
                deviceService.addDeviceToZone(radioactivity, bioLab.getId());
            }

            if (engineRoom.getDevices() == null || engineRoom.getDevices().isEmpty()) {
                Device gen = DeviceCategory.GENERATOR.createInstance();
                gen.setName("Moteur Principal");
                deviceService.addDeviceToZone(gen, engineRoom.getId());
            }

            if (reactorRoom.getDevices() == null || reactorRoom.getDevices().isEmpty()) {
                Device reactor = DeviceCategory.GENERATOR.createInstance();
                Device auxiliary_reactor = DeviceCategory.GENERATOR.createInstance();
                reactor.setName("Réacteur Principal");
                auxiliary_reactor.setName("Moteur Auxiliaire");
                deviceService.addDeviceToZone(reactor, reactorRoom.getId());
                deviceService.addDeviceToZone(auxiliary_reactor, reactorRoom.getId());
            }

            if (communicationRoom.getDevices() == null || communicationRoom.getDevices().isEmpty()) {
                Device radio = DeviceCategory.RADIO.createInstance();
                Device radar = DeviceCategory.RADAR.createInstance();
                Device cafe = DeviceCategory.CAFE.createInstance();
                radar.setName("Radar");
                radio.setName("Radio Orbital");
                cafe.setName("Machine à Café");
                deviceService.addDeviceToZone(radio, communicationRoom.getId());
                deviceService.addDeviceToZone(radar, communicationRoom.getId());
                deviceService.addDeviceToZone(cafe, communicationRoom.getId());
            }

            if (oxygenGeneratorRoom.getDevices() == null || oxygenGeneratorRoom.getDevices().isEmpty()) {
                Device oxygen_sensor = DeviceCategory.OXYGEN_SENSOR.createInstance();
                Device co2_sensor = DeviceCategory.CO2_SENSOR.createInstance();
                oxygen_sensor.setName("Capteur d'Oxygène");
                co2_sensor.setName("Capteur de CO2");
                deviceService.addDeviceToZone(oxygen_sensor, oxygenGeneratorRoom.getId());
                deviceService.addDeviceToZone(co2_sensor, oxygenGeneratorRoom.getId());
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