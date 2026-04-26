package io.squid.CyStation.controller;
import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.model.User;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    ZoneRepository zoneRepository;


    @GetMapping("/")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/admin";
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


}