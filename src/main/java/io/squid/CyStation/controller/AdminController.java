package io.squid.CyStation.controller;
import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.RequestStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.UserService;
import io.squid.CyStation.service.ZoneService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    DeviceService deviceService;

    @Autowired
    ZoneService zoneService;
    @Autowired
    private DeviceRepository deviceRepository;


    @GetMapping("/")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user";
    }

    @PostMapping("/users/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/add-exp")
    @ResponseBody
    public ResponseEntity<?> addExperience(@RequestParam Long id, @RequestParam int amount) {

        int newExp = userService.addExpAndLevelUp(id, amount);
        return ResponseEntity.ok(newExp);
    }

    @GetMapping("/zone")
    public String showMissionPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("deviceCategories", DeviceCategory.values());
        return "admin/zone";
    }

    @GetMapping("/user")
    public String showUserPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("deviceCategories", DeviceCategory.values());
        return "admin/user";
    }

    @PostMapping("/device/create")
    public String createDeviceAdmin(@RequestParam String name,
                                    @RequestParam String deviceType,
                                    @RequestParam Long zoneId) {
        try {
            Device newDevice = DeviceCategory.valueOf(deviceType.toUpperCase()).createInstance();
            newDevice.setName(name);
            deviceService.addDeviceToZone(newDevice, zoneId);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/zone?error";
        }
        return "redirect:/admin/zone";
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/request")
    public String showRequests(Model model) {

        List<Device> requests = deviceRepository.findByRequestStatus(RequestStatus.PENDING);

        model.addAttribute("pendingRequests", requests);
        model.addAttribute("activeTab", "requests");
        return "admin/request"; // Assurez-vous que le nom correspond à votre fichier HTML
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/request/process-deletion/{id}")
    @ResponseBody
    public ResponseEntity<?> processDeletion(@PathVariable Long id, @RequestParam boolean approved) {
        Device device = deviceRepository.findById(id).orElseThrow();

        if (approved) {
            deviceService.deleteDevice(device.getId());
        } else {
            device.setRequestStatus(RequestStatus.REJECTED);
            device.setRequestedBy(null);
            deviceRepository.save(device);
        }
        return ResponseEntity.ok().build();
    }
}

