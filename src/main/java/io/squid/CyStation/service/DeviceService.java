package io.squid.CyStation.service;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;

public class DeviceService {

    private final DeviceRepository deviceRepository;

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

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
