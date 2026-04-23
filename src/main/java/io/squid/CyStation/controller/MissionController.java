package io.squid.CyStation.controller;

import io.squid.CyStation.model.Zone;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MissionController {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/mission")
    public String showMissionPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        return "public/mission";
    }

    @PostMapping("/mission/zone/create")
    public String createZone(@RequestParam String name, @RequestParam(required = false) List<String> deviceNames) {
        Zone newZone = new Zone();
        newZone.setName(name);
        Zone savedZone = zoneRepository.save(newZone);

        if (deviceNames != null) {
            for (String dName : deviceNames) {
                if (dName != null && !dName.trim().isEmpty()) {
                    Device device = new Device();
                    device.setName(dName.trim());
                    device.setStatus(DeviceStatus.ONLINE);
                    device.setZone(savedZone);
                    deviceRepository.save(device);
                }
            }
        }
        return "redirect:/mission";
    }
}