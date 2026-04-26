package io.squid.CyStation.controller;

import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.*;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.ZoneService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ZoneController {

    private final ZoneRepository zoneRepository;
    private final ZoneService zoneService;
    private final DeviceRepository deviceRepository;
    private final DeviceService deviceService;


    public ZoneController(ZoneRepository zoneRepository,
                          ZoneService zoneService,
                          DeviceRepository deviceRepository,
                          DeviceService deviceService) {
        this.zoneRepository = zoneRepository;
        this.zoneService = zoneService;
        this.deviceRepository = deviceRepository;
        this.deviceService = deviceService;
    }


    @GetMapping("/mission")
    @PreAuthorize("isAuthenticated()")
    public String showMissionPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("deviceCategories", DeviceCategory.values());
        return "public/zone-overview";
    }


    @PostMapping("/mission/zone/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createZone(@RequestParam String name,
                             @RequestParam String description,
                             HttpServletRequest request) { // Ajout de la requête

        if (zoneRepository.findZoneByName(name) != null) {

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/mission") + "?error";

        } else {
            Zone newZone = new Zone();
            newZone.setName(name);
            newZone.setDescription(description);
            zoneService.save(newZone);

            String referer = request.getHeader("Referer");

            return "redirect:" + (referer != null ? referer : "/mission");
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SCIENTIST')")

    public String createDevice(@RequestParam String name,
                               @RequestParam String deviceType,
                               @RequestParam Long zoneId,
                               HttpServletRequest request) {

        try {
            Device newDevice = DeviceCategory.valueOf(deviceType.toUpperCase()).createInstance();
            newDevice.setName(name);
            newDevice.setStatus(DeviceStatus.ONLINE);

            deviceService.addDeviceToZone(newDevice, zoneId);

        } catch (IllegalArgumentException e) {
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/mission/zone/" + zoneId);
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/mission/zone/" + zoneId);
    }


    @PostMapping("/mission/device/toggle/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SCIENTIST')")
    @ResponseBody
    public DeviceStatus toggleDevice(@PathVariable Long id) {

        return deviceService.toggleStatus(id);
    }

    @PostMapping("/mission/device/repair/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Device repairDevice(@PathVariable Long id) {

        Device device = deviceService.repair(id);
        return device;
    }


    @PostMapping("/mission/device/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SCIENTIST')")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
        if (deviceRepository.existsById(id)) {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok("DELETED");
        }
        return ResponseEntity.status(404).body("NOT_FOUND");
    }

    @PostMapping("/mission/zone/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok("ZONE_DELETED");
    }


    @GetMapping("/mission/game/co2")
    @PreAuthorize("isAuthenticated()")
    public String showCo2Game() {

        return "engineer/co2sensor.html";
    }


    @GetMapping("/mission/game/reactor")
    @PreAuthorize("isAuthenticated()")
    public String showReactorGame() {

        return "engineer/reactor.html";
    }

    @GetMapping("/mission/game/radar")
    @PreAuthorize("isAuthenticated()")
    public String showRadarGame() {

        return "engineer/radar.html";
    }


    @GetMapping("/mission/game/radio")
    @PreAuthorize("isAuthenticated()")
    public String showRadioGame() {

        return "engineer/radio.html";
    }

    @GetMapping("/mission/game/o2")
    @PreAuthorize("isAuthenticated()")
    public String showO2Game() {

        return "engineer/hydroponic.html";
    }

    @GetMapping("/mission/game/radiation")
    public String showRadiationGame() {

        return "engineer/radiation.html";
    }

    @GetMapping("/mission/game/water")
    public String showWaterGame() {

        return "engineer/water.html";
    }


    @GetMapping("/mission/zone/{id}")
    @PreAuthorize("isAuthenticated()")
    public String viewZone(@PathVariable Long id, Model model) {

        Zone zone = zoneRepository.findById(id).orElseThrow();
        model.addAttribute("zone", zone);
        model.addAttribute("deviceCategories", DeviceCategory.values());

        return "public/zone-detail";
    }

    @PostMapping("/admin/zone/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createZoneAdmin(@RequestParam String name,
                                  @RequestParam(required = false) String description,
                                  @RequestParam(required = false) String imageUrl,
                                  HttpServletRequest request) {

        if (zoneRepository.findZoneByName(name) == null) {
            Zone newZone = new Zone();
            newZone.setName(name);
            newZone.setDescription(description);
            newZone.setImageUrl(imageUrl);
            zoneService.save(newZone);
        }

        return "redirect:/admin/zone";
    }


}