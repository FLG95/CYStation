package io.squid.CyStation.config;

import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.enums.Gender;
import io.squid.CyStation.enums.Role;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.User;
import io.squid.CyStation.model.Zone;
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
                    "Recherche sur la survie végétale en milieu hostile.", "https://img.stablecog.com/insecure/256w/aHR0cHM6Ly9iLnN0YWJsZWNvZy5jb20vMGVhODVkODktMzk5NC00ZTkyLWIzNmEtZGQ0NjM1OGZkZGQxLmpwZWc.webp");

            Zone engineRoom = createZoneIfNotFound(zoneRepository, "Salle des Moteurs",
                    "Propulsion ionique et gestion de l'énergie.", "https://files.idyllic.app/files/static/3503528?width=256&optimizer=image");

            Zone reactorRoom = createZoneIfNotFound(zoneRepository, "Salle du Réacteur",
                    "Génération d'energie pour alimenter tout les systèmes de la station.", "https://img.stablecog.com/insecure/1536w/aHR0cHM6Ly9iLnN0YWJsZWNvZy5jb20vM2QxZTFiNWQtZDgxMS00NWE2LWFmOGYtM2I5NWRhMzcyN2JmLmpwZWc.webp");

            Zone communicationRoom = createZoneIfNotFound(zoneRepository, "Salle des Communications",
                    "Gères toutes les communications et signaux entrants et sortant de la station", "https://files.idyllic.app/files/static/36807?width=640&optimizer=image");

            Zone oxygenGeneratorRoom = createZoneIfNotFound(zoneRepository, "Salle du Générateur d'Oxygène",
                    "Génères l'oxygène requis à la survie de l'équipage", "https://images.stockcake.com/public/a/b/c/abca9bb9-562b-469a-b52e-e963596e52e0_medium/geodesic-greenhouse-office-stockcake.jpg");

            Zone navigationRoom = createZoneIfNotFound(zoneRepository, "Salle de Navigation",
                    "Gères la position et les moteurs de la station pour maintenir l'orbite", "https://images.stockcake.com/public/5/a/a/5aad43e7-c898-400e-8257-fd83d7de9c7b_medium/futuristic-control-room-stockcake.jpg");

            Zone cafeteriaRoom = createZoneIfNotFound(zoneRepository, "Caféteria",
                    "Permet de se restaurer dans la station", "https://files.idyllic.app/files/static/37615?width=640&optimizer=image");

            Zone adminRoom = createZoneIfNotFound(zoneRepository, "Administration",
                    "Gères les divers aspects administratif de la station", "https://images.stockcake.com/public/a/2/5/a257662f-f153-4457-a2c9-f667532b07ac_large/futuristic-control-room-stockcake.jpg");


            if (bioLab.getDevices() == null || bioLab.getDevices().isEmpty()) {
                Device genenerator = DeviceCategory.GENERATOR.createInstance();
                Device cafe = DeviceCategory.COFFEE.createInstance();
                Device radioactivity = DeviceCategory.RADIATION_SENSOR.createInstance();
                cafe.setName("Machine à Café");
                genenerator.setName("Générateur Secondaire");
                radioactivity.setName("Radiation de l'échantillon");
                deviceService.addDeviceToZone(genenerator, bioLab.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(cafe, bioLab.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(radioactivity, bioLab.getId(), DeviceStatus.OFFLINE);
            }

            if (adminRoom.getDevices() == null || adminRoom.getDevices().isEmpty()) {
                Device genenerator = DeviceCategory.GENERATOR.createInstance();
                Device cafe = DeviceCategory.COFFEE.createInstance();
                cafe.setName("Machine à Café");
                genenerator.setName("Générateur Secondaire");
                deviceService.addDeviceToZone(genenerator, adminRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(cafe, adminRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (cafeteriaRoom.getDevices() == null || cafeteriaRoom.getDevices().isEmpty()) {
                Device generator = DeviceCategory.GENERATOR.createInstance();
                Device cafe = DeviceCategory.COFFEE.createInstance();
                Device booze = DeviceCategory.BOOZE.createInstance();
                cafe.setName("Machine à Café");
                booze.setName("Frigo à bière");
                generator.setName("Générateur Secondaire");
                deviceService.addDeviceToZone(generator, cafeteriaRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(cafe, cafeteriaRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(booze, cafeteriaRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (navigationRoom.getDevices() == null || navigationRoom.getDevices().isEmpty()) {
                Device generator = DeviceCategory.GENERATOR.createInstance();
                Device cafe = DeviceCategory.COFFEE.createInstance();
                Device radio = DeviceCategory.RADIO.createInstance();
                Device radar = DeviceCategory.RADAR.createInstance();
                cafe.setName("Machine à Café");
                radio.setName("Radio");
                radar.setName("Radar");
                generator.setName("Générateur Secondaire");
                deviceService.addDeviceToZone(generator, navigationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(cafe, navigationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(radio, navigationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(radar, navigationRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (engineRoom.getDevices() == null || engineRoom.getDevices().isEmpty()) {
                Device gen = DeviceCategory.GENERATOR.createInstance();
                gen.setName("Générateur Secondaire");
                deviceService.addDeviceToZone(gen, engineRoom.getId(), DeviceStatus.OFFLINE);
                Device engine = DeviceCategory.GENERATOR.createInstance();
                engine.setName("Moteur");
                deviceService.addDeviceToZone(engine, engineRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (reactorRoom.getDevices() == null || reactorRoom.getDevices().isEmpty()) {
                Device generator = DeviceCategory.GENERATOR.createInstance();
                Device auxiliary_reactor = DeviceCategory.GENERATOR.createInstance();
                generator.setName("Générateur Principal");
                auxiliary_reactor.setName("Générateur Auxiliaire");
                deviceService.addDeviceToZone(generator, reactorRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(auxiliary_reactor, reactorRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (communicationRoom.getDevices() == null || communicationRoom.getDevices().isEmpty()) {
                Device generator = DeviceCategory.GENERATOR.createInstance();
                Device radio = DeviceCategory.RADIO.createInstance();
                Device radar = DeviceCategory.RADAR.createInstance();
                Device cafe = DeviceCategory.COFFEE.createInstance();
                generator.setName("Générateur Secondaire");
                radar.setName("Radar");
                radio.setName("Radio Orbital");
                cafe.setName("Machine à Café");
                deviceService.addDeviceToZone(generator, communicationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(radio, communicationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(radar, communicationRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(cafe, communicationRoom.getId(), DeviceStatus.OFFLINE);
            }

            if (oxygenGeneratorRoom.getDevices() == null || oxygenGeneratorRoom.getDevices().isEmpty()) {
                Device generator = DeviceCategory.GENERATOR.createInstance();
                Device oxygen_sensor = DeviceCategory.OXYGEN_SENSOR.createInstance();
                Device co2_sensor = DeviceCategory.CO2_SENSOR.createInstance();
                generator.setName("Générateur Secondaire");
                oxygen_sensor.setName("Capteur d'Oxygène");
                co2_sensor.setName("Capteur de CO2");
                deviceService.addDeviceToZone(generator, oxygenGeneratorRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(oxygen_sensor, oxygenGeneratorRoom.getId(), DeviceStatus.OFFLINE);
                deviceService.addDeviceToZone(co2_sensor, oxygenGeneratorRoom.getId(), DeviceStatus.OFFLINE);
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
            user.setEnable(true);

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