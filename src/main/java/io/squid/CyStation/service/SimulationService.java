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
            if (device.getStatus() == DeviceStatus.ONLINE) {
                boolean isUpdated = device.updateTelemetry();


                if (isUpdated) {
                    dbNeedsUpdate = true;
                    messagingTemplate.convertAndSend("/topic/device-telemetry", device);
                }
            }
        }
        if (dbNeedsUpdate) {
            deviceRepository.saveAllAndFlush(devices);
        }
    }

}
