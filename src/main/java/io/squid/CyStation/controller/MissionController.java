package io.squid.CyStation.controller;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Co2Sensor;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.DeviceService;
import io.squid.CyStation.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "public/mission";
    }

    @PostMapping("/mission/zone/create")
    public String createZone(@RequestParam String name) {

        if (zoneRepository.findZoneByName(name) != null) {

            return "redirect:/mission?error";

        } else {

            Zone newZone = new Zone();
            newZone.setName(name);
            zoneService.save(newZone);

            return "redirect:/mission";

        }
    }

    @PostMapping("/mission/device/create")
    public String createDevice(@RequestParam String name, @RequestParam String deviceType, @RequestParam Long zoneId) {

        Device newDevice;
        switch (deviceType) {
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


    @GetMapping("/mission/game/Co2")
    public String showCo2Game() {

        return "engineer/hydroponic.html";
    }


}