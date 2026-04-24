package io.squid.CyStation.service;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.User;
import io.squid.CyStation.model.Zone;
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


    public DeviceService(DeviceRepository deviceRepository, ZoneRepository zoneRepository) {
        this.deviceRepository = deviceRepository;
        this.zoneRepository = zoneRepository;
    }


    @Transactional
    public void deleteDevice(Long id) {

        Device device = deviceRepository.findById(id).orElse(null);
        if (device != null) {

            if (device.getZone() != null) {
                device.getZone().getDevices().remove(device);
            }
            deviceRepository.delete(device);
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

        if (device.getStatus() == DeviceStatus.MAINTENANCE) {

            return device.getStatus();
        }

        if (device.getStatus() == DeviceStatus.ONLINE) {
            device.setStatus(DeviceStatus.OFFLINE);
        } else {
            device.setStatus(DeviceStatus.ONLINE);
        }

        deviceRepository.save(device);

        return device.getStatus();
    }

    @Transactional
    public Device repair(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow();
        if (device.getStatus() == DeviceStatus.MAINTENANCE) {
            device.setStatus(DeviceStatus.OFFLINE);
            deviceRepository.save(device);
        }
        return device;
    }



}
