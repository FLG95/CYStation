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

    @Autowired
    private ZoneRepository zoneRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }


    public void updateDeviceStatus(Long id, DeviceStatus status) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appareil introuvable avec l'ID : " + id));

        device.setStatus(status);
        deviceRepository.save(device);
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
        device.setStatus(DeviceStatus.OFFLINE);

        deviceRepository.save(device);
    }


}
