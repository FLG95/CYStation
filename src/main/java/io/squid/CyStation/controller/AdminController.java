package io.squid.CyStation.controller;

import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.enums.RequestStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Generator;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.ArticleService;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.UserService;
import io.squid.CyStation.service.ZoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private  SimpMessagingTemplate messagingTemplate;




    @GetMapping("/")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("activeTab", "users");
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
        model.addAttribute("activeTab", "zones");
        return "admin/zone";
    }

    @GetMapping("/user")
    public String showUserPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("deviceCategories", DeviceCategory.values());
        model.addAttribute("activeTab", "users");
        return "admin/user";
    }

    @GetMapping("/articles")
    public String showArticlesPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        model.addAttribute("activeTab", "articles");
        return "admin/articles";
    }

    @DeleteMapping("/articles/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        try {
            articleService.getArticleById(id);
            articleService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/device/create")
    public String createDeviceAdmin(@RequestParam String name,
                                    @RequestParam String deviceType,
                                    @RequestParam Long zoneId) {
        try {
            Device newDevice = DeviceCategory.valueOf(deviceType.toUpperCase()).createInstance();
            newDevice.setName(name);
            deviceService.addDeviceToZone(newDevice, zoneId, DeviceStatus.MAINTENANCE);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/zone?error";
        }
        return "redirect:/admin/zone";
    }

    @PostMapping("/mission/zone/create")
    public String createZone(@RequestParam String name,
                             @RequestParam String description,
                             HttpServletRequest request) {

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

    @GetMapping("/request")
    public String showRequests(Model model) {
        List<Device> requests = deviceRepository.findByRequestStatus(RequestStatus.PENDING);
        model.addAttribute("pendingRequests", requests);
        model.addAttribute("activeTab", "requests");
        return "admin/request";
    }

    @PostMapping("/request/process-deletion/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> processDeletion(@PathVariable Long id, @RequestParam boolean approved) {
        try {
            Device device = deviceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Device introuvable"));

            if (approved) {
                if (device.getZone() != null) {
                    device.getZone().getDevices().remove(device);
                }
                deviceService.deleteDevice(device.getId());
                return ResponseEntity.ok().build();
            } else {
                device.setRequestStatus(RequestStatus.REJECTED);
                device.setRequestedBy(null);
                deviceRepository.save(device);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        }
    }


    @PostMapping("/user/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        try {
            String action = userService.toggleUserStatus(id);
            Map<String, Object> response = new HashMap<>();
            response.put("action", action);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur");
        }
    }


    @PostMapping("/device/maintenance/{id}")
    @ResponseBody
    public ResponseEntity<String> sabotageDevice(@PathVariable Long id) {
        Device device = deviceRepository.findById(id).orElseThrow();

        device.setStatus(DeviceStatus.MAINTENANCE);

        if (device instanceof Generator) {
            ((Generator) device).setProduction(0);
        }

        deviceRepository.save(device);
        messagingTemplate.convertAndSend("/topic/device-status", device);
        deviceService.notifyEnergyUpdate(device.getZone());

        if (device instanceof Generator) {
            ((Generator) device).setProduction(0);
            deviceRepository.save(device);
            deviceService.handleBlackout(device.getZone());
        }

        return ResponseEntity.ok("Module mit en maintenance avec succès");
    }

}