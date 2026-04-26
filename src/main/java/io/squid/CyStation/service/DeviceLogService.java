package io.squid.CyStation.service;

import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.DeviceLog;
import io.squid.CyStation.repository.DeviceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceLogService {

    private final DeviceLogRepository logRepository;

    public DeviceLogService(DeviceLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logTelemetry(Device device, String metric, Double oldV, Double newV) {
        logRepository.save(new DeviceLog(device, "TELEMETRY", oldV, newV, metric));
    }

    public void logStatusChange(Device device, String oldStatus, String newStatus) {
        logRepository.save(new DeviceLog(device, "STATUS_CHANGE", oldStatus, newStatus, "STATUS"));
    }

    public void logRepair(Device device) {
        logRepository.save(new DeviceLog(device, "REPAIR", "MAINTENANCE", "ONLINE", "REPAIR_ACTION"));
    }

    public void logDeletion(Device device) {
        String currentStatus = device.getStatus() != null ? device.getStatus().toString() : "UNKNOWN";
        logRepository.save(new DeviceLog(device, "DELETE", currentStatus, "DELETED", "ADMIN_ACTION"));
    }

    public void clearAllLogs() {
        logRepository.deleteAllInBatch();
    }

    public List<DeviceLog> getLogsByDeviceId(Long deviceId) {
        return logRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void purgeOldLogs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        logRepository.deleteLogsOlderThan(cutoff);
        System.out.println("Purge des logs effectuée — Seuil : " + cutoff);
    }
}
