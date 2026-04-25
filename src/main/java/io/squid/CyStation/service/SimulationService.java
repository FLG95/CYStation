package io.squid.CyStation.service;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.*;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 40000)
    @Transactional
    public void actualiserStatusMachines() {
        List<Zone> zones = zoneRepository.findAll();
        boolean hasChanges = false;

        for (Zone zone : zones) {
            for (Device device : zone.getDevices()) {
                if (device.getStatus() == DeviceStatus.ONLINE && Math.random() < 0.05) {
                    device.setStatus(DeviceStatus.MAINTENANCE);
                    hasChanges = true;

                    messagingTemplate.convertAndSend("/topic/device-status", device);
                }
            }
        }

        if (hasChanges) {

            zoneRepository.saveAllAndFlush(zones);
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void actualiserTelemetrieMachines() {
        List<Device> devices = deviceRepository.findAll();
        boolean dbNeedsUpdate = false;

        for (Device device : devices) {
            boolean isUpdated = false;

            if (device.getStatus() == DeviceStatus.ONLINE) {

                if (device instanceof Co2Sensor co2Sensor) {
                    co2Sensor.setPpmLevel(400 + (int)(Math.random() * 400)); // entre 400 et 800 ppm
                    isUpdated = true;
                }
                else if (device instanceof Radar radar) {
                    radar.setDetected((int)(Math.random() * 6)); // entre 0 et 5
                    isUpdated = true;
                }
                else if (device instanceof Generator generator) {
                    generator.setProductionValue(80 + (int)(Math.random() * 20)); // entre 80 et 100
                    isUpdated = true;
                }
                else if (device instanceof Radio radio) {
                    radio.setContact(Math.random() > 0.5 ? 1 : 0); // entre 0 et 1
                    isUpdated = true;
                }

                if (isUpdated) {
                    dbNeedsUpdate = true;
                    messagingTemplate.convertAndSend("/topic/device-telemetry", device);
                }
            }
        }

        // Sauvegarde globale uniquement si au moins un appareil a changé
        if (dbNeedsUpdate) {
            deviceRepository.saveAllAndFlush(devices);
        }
    }

}
