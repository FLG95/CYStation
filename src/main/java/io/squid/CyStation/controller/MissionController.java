package io.squid.CyStation.controller;

import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.ZoneRepository;
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
}