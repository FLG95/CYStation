package io.squid.CyStation.repository;

import io.squid.CyStation.enums.DeviceStatus;
import io.squid.CyStation.enums.RequestStatus;
import io.squid.CyStation.model.Co2Sensor;
import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {


    List<Device> findByZoneAndStatus(Zone zone, DeviceStatus status);

    public Device findDeviceById(Long id);

    List<Device>findByRequestStatus(RequestStatus requestStatus);
}
