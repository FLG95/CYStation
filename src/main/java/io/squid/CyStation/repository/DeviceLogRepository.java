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
}