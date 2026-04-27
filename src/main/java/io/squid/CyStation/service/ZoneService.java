package io.squid.CyStation.service;

import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.DeviceLogRepository;
import io.squid.CyStation.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service

public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final DeviceLogRepository deviceLogRepository;

    public ZoneService( ZoneRepository zoneRepository, DeviceLogRepository deviceLogRepository){
        this.zoneRepository = zoneRepository;
        this.deviceLogRepository = deviceLogRepository;
    }



    public void save( Zone zone){

        zoneRepository.save(zone);
    }


    @Transactional
    public void deleteZone(Long id) {
        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone == null) return;

        for (Device device : zone.getDevices()) {
            deviceLogRepository.deleteByDeviceId(device.getId());
        }
        deviceLogRepository.flush();
        zoneRepository.delete(zone);
    }
}
