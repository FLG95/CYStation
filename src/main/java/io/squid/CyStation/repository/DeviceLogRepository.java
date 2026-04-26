package io.squid.CyStation.repository;

import io.squid.CyStation.model.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceLog d WHERE d.timestamp < :cutoffDate")
    void deleteLogsOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    List<DeviceLog> findByDeviceIdOrderByTimestampDesc(Long deviceId);

    List<DeviceLog> findByDeviceIdAndTimestampAfterOrderByTimestampDesc(Long deviceId, LocalDateTime time);
    List<DeviceLog> findTop50ByDeviceIdAndEventTypeNotOrderByTimestampDesc(Long deviceId, String excludedType);

    List<DeviceLog> findByTimestampAfterOrderByTimestampDesc(LocalDateTime startTime);

    List<DeviceLog> findTop50ByEventTypeNotOrderByTimestampDesc(String excludedEventType);

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceLog d WHERE d.device.id = :deviceId")
    void deleteByDeviceId(@Param("deviceId") Long deviceId);

    @Modifying
    @Query("UPDATE DeviceLog d SET d.device = null WHERE d.device.id = :deviceId")
    void detachLogsFromDevice(@Param("deviceId") Long deviceId);

}