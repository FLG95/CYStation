package io.squid.CyStation.service;

import io.squid.CyStation.DTO.EnergyUpdateDTO;
import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Generator;
import io.squid.CyStation.model.User;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceLogRepository;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ZoneRepository zoneRepository;
    private final DeviceLogService deviceLogService;
    private final DeviceLogRepository deviceLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public DeviceService(DeviceRepository deviceRepository,
                         ZoneRepository zoneRepository,
                         DeviceLogService deviceLogService,
                         DeviceLogRepository deviceLogRepository,
                         SimpMessagingTemplate messagingTemplate) {
        this.deviceRepository = deviceRepository;
        this.zoneRepository = zoneRepository;
        this.deviceLogService = deviceLogService;
        this.deviceLogRepository = deviceLogRepository;
        this.messagingTemplate = messagingTemplate;
    }


    @Transactional
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id).orElse(null);

        if (device != null) {
            Zone zone = device.getZone();

            deviceLogRepository.deleteByDeviceId(id);

            if (zone != null) {
                zone.getDevices().remove(device);
                device.setZone(null);
            }


            deviceRepository.delete(device);
            deviceRepository.flush();

            if (zone != null) {
                handleBlackout(zone);
            }
        }
    }

    @Transactional
    public void addDeviceToZone(Device device, Long zoneId) {

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone introuvable"));

        device.setZone(zone);
        device.setStatus(DeviceStatus.MAINTENANCE);

        deviceRepository.save(device);
    }


    @Transactional
    public DeviceStatus toggleStatus(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow();
        Zone zone = device.getZone();

        if (device.getStatus() != DeviceStatus.ONLINE) {
            if (!(device instanceof Generator)) {
                double totalProd = zone.getTotalProduction();
                double totalConsum = zone.getTotalConsumption();

                if (totalProd <= 0 || totalConsum + device.getConsumptionValue() > totalProd) {
                    throw new RuntimeException("Énergie insuffisante !");
                }
            }
            device.setStatus(DeviceStatus.ONLINE);

            if (device instanceof Generator) {
                device.updateTelemetry();
            }

        } else {
            device.setStatus(DeviceStatus.OFFLINE);

            if (device instanceof Generator) {
                ((Generator) device).setProduction(0);
                deviceRepository.save(device);
                handleBlackout(zone);
            }
        }

        deviceRepository.save(device);
        messagingTemplate.convertAndSend("/topic/device-status", device);
        notifyEnergyUpdate(zone);

        return device.getStatus();
    }

    public void handleBlackout(Zone zone) {
        double totalProd = zone.getTotalProduction();

        List<Device> activeDevices = zone.getDevices().stream()
                .filter(d -> d.getStatus() == DeviceStatus.ONLINE && !(d instanceof Generator))
                .toList();

        double currentConsum = activeDevices.stream()
                .mapToDouble(Device::getConsumptionValue)
                .sum();

        if (currentConsum > totalProd) {
            for (Device d : activeDevices) {
                d.setStatus(DeviceStatus.OFFLINE);
                deviceRepository.save(d);
                messagingTemplate.convertAndSend("/topic/device-status", d);
            }
        }
    }



    public void notifyEnergyUpdate(Zone zone) {

        double totalProd = zone.getDevices().stream()
                .filter(d -> d instanceof Generator)
                .mapToDouble(d -> ((Generator) d).getProductionValue())
                .sum();

        double totalConsum = zone.getDevices().stream()
                .mapToDouble(Device::getConsumptionValue)
                .sum();

        EnergyUpdateDTO update = new EnergyUpdateDTO(zone.getId(), totalProd, totalConsum);

        messagingTemplate.convertAndSend("/topic/energy-update", update);
    }




    @Transactional
    public Device repair(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow();
        if (device.getStatus() == DeviceStatus.MAINTENANCE) {
            device.setStatus(DeviceStatus.OFFLINE);
            deviceRepository.save(device);
            deviceLogService.logRepair(device);
        }
        return device;
    }

}
