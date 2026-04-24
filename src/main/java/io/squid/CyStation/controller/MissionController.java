package io.squid.CyStation.controller;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Co2Sensor;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
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
        Zone newZone = new Zone();
        newZone.setName(name);
        zoneRepository.save(newZone);

        return "redirect:/mission";
    }

    @PostMapping("/mission/device/create")
    public String createDevice(@RequestParam String name,@RequestParam String deviceType, @RequestParam Long zoneId) {
        Device newDevice;
        switch(deviceType){
            case "CO2_SENSOR":
                newDevice = new Co2Sensor();
                break;
            default:
                throw new IllegalArgumentException("Type de module non reconnu : " + deviceType);
        }
        newDevice.setName(name);
        newDevice.setStatus(DeviceStatus.ONLINE);
        deviceService.addDeviceToZone(newDevice, zoneId);

        return "redirect:/mission";
    }
}