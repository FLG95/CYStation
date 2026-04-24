package io.squid.CyStation.controller;

import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MissionController {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/mission")
    public String showMissionPage(Model model) {
        model.addAttribute("zones", zoneRepository.findAll());
        return "public/mission";
    }

    @PostMapping("/mission/zone/create")
    public String createZone(@RequestParam String name) {

        if (zoneRepository.findZoneByName(name) != null ){

            return "redirect:/mission?error";

        }else{

            Zone newZone = new Zone();
            newZone.setName(name);
            zoneService.save(newZone);

            return "redirect:/mission";

        }
    }

    @PostMapping("/mission/device/create")
    public String createDevice(@RequestParam String name, @RequestParam Long zoneId) {
        Device newDevice = new Device();
        newDevice.setName(name);

        deviceService.addDeviceToZone(newDevice, zoneId);

        return "redirect:/mission";
    }
}