package io.squid.CyStation.service;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.User;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceLogRepository;
import io.squid.CyStation.repository.DeviceRepository;
import io.squid.CyStation.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ZoneRepository zoneRepository;
    private final DeviceLogService deviceLogService;
    private final DeviceLogRepository deviceLogRepository;

    public DeviceService(DeviceRepository deviceRepository, ZoneRepository zoneRepository, DeviceLogService deviceLogService, DeviceLogRepository deviceLogRepository) {
        this.deviceRepository = deviceRepository;
        this.zoneRepository = zoneRepository;
        this.deviceLogService = deviceLogService;
        this.deviceLogRepository = deviceLogRepository;
    }


    @Transactional
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id).orElse(null);

        if (device != null) {
            if (device.getZone() != null) {
                device.getZone().getDevices().remove(device);
            }
            deviceLogRepository.deleteByDeviceId(id);
            deviceRepository.delete(device);
            deviceRepository.flush();
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
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device non trouvé avec l'id : " + id));

        String oldStatus = device.getStatus().toString();

        if (device.getStatus() == DeviceStatus.MAINTENANCE) {

            return device.getStatus();
        }

        if (device.getStatus() == DeviceStatus.ONLINE) {
            device.setStatus(DeviceStatus.OFFLINE);
        } else {
            device.setStatus(DeviceStatus.ONLINE);
        }

        deviceRepository.save(device);
        deviceLogService.logStatusChange(device, oldStatus, device.getStatus().toString());
        return device.getStatus();
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
