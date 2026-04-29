package io.squid.CyStation.service;

import io.squid.CyStation.DTO.ZoneEnergyDTO;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.*;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationService {

    private final ZoneRepository zoneRepository;
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final DeviceLogService deviceLogService;

    public SimulationService(ZoneRepository zoneRepository, DeviceRepository deviceRepository, SimpMessagingTemplate messagingTemplate, DeviceLogService deviceLogService) {
        this.zoneRepository = zoneRepository;
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;
        this.deviceLogService = deviceLogService;
    }


    @Scheduled(initialDelay = 7000, fixedRate = 40000)
    @Transactional
    public void actualiserStatusMachines() {
        List<Zone> zones = zoneRepository.findAll();
        boolean hasChanges = false;

        for (Zone zone : zones) {
            double totalProduction = 0;
            double totalConsumption = 0;


            for (Device device : zone.getDevices()) {
                if (device instanceof Generator generator) {

                    totalProduction += generator.getProductionValue();
                } else {

                    totalConsumption += device.getConsumptionValue();
                }


            }


            if (totalProduction < totalConsumption) {
                for (Device device : zone.getDevices()) {

                    if (device.getStatus() == DeviceStatus.ONLINE) {
                        device.setStatus(DeviceStatus.OFFLINE);
                        hasChanges = true;
                        messagingTemplate.convertAndSend("/topic/device-status", device);
                    }

                    totalProduction = 0;
                    totalConsumption = 0;
                }
            } else {

                for (Device device : zone.getDevices()) {
                    if (device.getStatus() == DeviceStatus.ONLINE && Math.random() < 0.05 && !(device instanceof Generator)) {

                        device.setStatus(DeviceStatus.MAINTENANCE);
                        hasChanges = true;
                        messagingTemplate.convertAndSend("/topic/device-status", device);
                    }
                }
            }

            messagingTemplate.convertAndSend("/topic/zone-energy",
                    new ZoneEnergyDTO(zone.getId(), totalProduction, totalConsumption));
        }

        if (hasChanges) {
            zoneRepository.saveAllAndFlush(zones);
        }
    }

    @Scheduled(fixedRate = 15000)
    @Transactional
    public void actualiserTelemetrieMachines() {
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            try {
                // On récupère les métriques avant toute mise à jour
                Map<String, Double> oldMetrics = device.getTelemetryMetrics();

                boolean isOnline = device.getStatus() == DeviceStatus.ONLINE;
                boolean isUpdated = false;

                if (isOnline) {
                    isUpdated = device.updateTelemetry();
                }

                // On récupère les métriques après mise à jour potentielle
                Map<String, Double> newMetrics = device.getTelemetryMetrics();

                // On compare et on log
                for (String metricName : newMetrics.keySet()) {
                    Double oldVal = oldMetrics.get(metricName);
                    Double newVal = newMetrics.get(metricName);

                    // Pour les métriques d'énergie, on force le log si l'appareil est ONLINE
                    // pour s'assurer qu'elles apparaissent dans les rapports (valeurs souvent fixes)
                    boolean isEnergyMetric = "CONSUMPTION_VALUE".equals(metricName) || "PRODUCTION_VALUE".equals(metricName);

                    if (oldVal == null || !oldVal.equals(newVal) || (isEnergyMetric && isOnline)) {
                        deviceLogService.logTelemetry(device, metricName, oldVal, newVal);
                    }
                }

                if (isOnline && isUpdated) {
                    messagingTemplate.convertAndSend("/topic/device-telemetry", device);

                    if (device.getZone() != null) {
                        broadcastZoneEnergy(device.getZone());
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur de télémétrie sur l'appareil ID " + device.getId() + " — " + e.getMessage());
            }
        }
    }

    private void broadcastZoneEnergy(Zone zone) {
        double totalProduction = zone.getDevices().stream()
                .filter(d -> d instanceof Generator)
                .mapToDouble(d -> ((Generator) d).getProductionValue()).sum();

        double totalConsumption = zone.getDevices().stream()
                .filter(d -> !(d instanceof Generator))
                .mapToDouble(Device::getConsumptionValue).sum();

        messagingTemplate.convertAndSend("/topic/zone-energy",
                new ZoneEnergyDTO(zone.getId(), totalProduction, totalConsumption));
    }

}
