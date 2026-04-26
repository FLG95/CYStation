package io.squid.CyStation.service;

import io.squid.CyStation.model.Device;
import io.squid.CyStation.model.DeviceLog;
import io.squid.CyStation.repository.DeviceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceLogService {

    private final DeviceLogRepository logRepository;

    public DeviceLogService(DeviceLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private String getCurrentOperator() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return auth.getName();
        }
        return "SYSTEM";
    }

    public void logTelemetry(Device device, String metric, Double oldV, Double newV) {
        logRepository.save(new DeviceLog(device, "TELEMETRY", oldV, newV, metric));
    }

    public void logStatusChange(Device device, String oldStatus, String newStatus) {
        DeviceLog log = new DeviceLog(device, "STATUS_CHANGE", oldStatus, newStatus, "STATUS");
        log.setOperatorName(getCurrentOperator());
        logRepository.save(log);
    }

    public void logRepair(Device device) {
        DeviceLog log = new DeviceLog(device, "REPAIR", "MAINTENANCE", "ONLINE", "REPAIR_ACTION");
        log.setOperatorName(getCurrentOperator());
        logRepository.save(log);
    }

    public void logDeletion(Device device) {
        DeviceLog log = new DeviceLog(device, "DELETE", device.getStatus().toString(), "DELETED", "ADMIN_ACTION");
        log.setOperatorName(getCurrentOperator());
        logRepository.save(log);
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
