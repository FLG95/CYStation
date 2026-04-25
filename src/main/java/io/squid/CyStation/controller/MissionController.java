package io.squid.CyStation.controller;

import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.*;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MissionController {

    private final ZoneRepository zoneRepository;
    private final ZoneService zoneService;
    private final DeviceRepository deviceRepository;
    private final DeviceService deviceService;


    public MissionController(ZoneRepository zoneRepository,
                             ZoneService zoneService,
                             DeviceRepository deviceRepository,
                             DeviceService deviceService){
        this.zoneRepository = zoneRepository;
        this.zoneService = zoneService;
        this.deviceRepository = deviceRepository;
        this.deviceService = deviceService;
    }


    @GetMapping("/mission")
    public String showMissionPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("deviceCategories", DeviceCategory.values());
        return "public/mission";
    }

    @PostMapping("/mission/zone/create")
    public String createZone(@RequestParam String name, @RequestParam String description) {

        if (zoneRepository.findZoneByName(name) != null) {

            return "redirect:/mission?error";

        } else {

            Zone newZone = new Zone();
            newZone.setName(name);
            newZone.setDescription(description);
            zoneService.save(newZone);

            return "redirect:/mission";

        }
    }

    @PostMapping("mission/zone/update")
    @ResponseBody
    public ResponseEntity<String> updateZone(@RequestParam Long id,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String description) {
        Zone zone = zoneRepository.findZoneById(id);

        if (zone == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zone non trouvée");
        }

        if (name != null && !name.isEmpty()) zone.setName(name);
        if (description != null) zone.setDescription(description);

        zoneService.save(zone);
        return ResponseEntity.ok("Zone mise à jour avec succès");
    }



    @PostMapping("/mission/device/create")
    public String createDevice(@RequestParam String name, @RequestParam String deviceType, @RequestParam Long zoneId) {

        try {
            Device newDevice = DeviceCategory.valueOf(deviceType.toUpperCase()).createInstance();

            newDevice.setName(name);
            newDevice.setStatus(DeviceStatus.ONLINE);

            deviceService.addDeviceToZone(newDevice, zoneId);

        } catch (IllegalArgumentException e) {
            // Redirection propre si la catégorie n'existe pas dans l'Enum
            return "redirect:/mission?error=invalid_device_type";
        }

        return "redirect:/mission";
    }


    @PostMapping("/mission/device/toggle/{id}")
    @ResponseBody
    public DeviceStatus toggleDevice(@PathVariable Long id) {

        return deviceService.toggleStatus(id);
    }

    @PostMapping("/mission/device/repair/{id}")
    @ResponseBody
    public Device repairDevice(@PathVariable Long id) {

        Device device = deviceService.repair(id);
        return device;
    }


    @PostMapping("/mission/device/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
        if (deviceRepository.existsById(id)) {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok("DELETED");
        }
        return ResponseEntity.status(404).body("NOT_FOUND");
    }

    @PostMapping("/mission/zone/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok("ZONE_DELETED");
    }


    @GetMapping("/mission/game/co2")
    public String showCo2Game() {

        return "engineer/hydroponic.html";
    }


    @GetMapping("/mission/game/reactor")
    public String showReactorGame() {

        return "engineer/reactor.html";
    }

    @GetMapping("/mission/game/radar")
    public String showRadarGame() {

        return "engineer/radar.html";
    }


    @GetMapping("/mission/game/radio")
    public String showRadioGame() {

        return "engineer/radio.html";
    }

    @GetMapping("/mission/zone/{id}")
    public String viewZone(@PathVariable Long id, Model model) {

        Zone zone = zoneRepository.findById(id).orElseThrow();
        model.addAttribute("zone", zone);

        return "public/zone-detail";
    }


}