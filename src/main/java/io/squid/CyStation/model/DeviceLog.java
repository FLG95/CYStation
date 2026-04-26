package io.squid.CyStation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeviceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String eventType;

    private Double oldNumericValue;
    private Double newNumericValue;

    private String oldStringValue;
    private String newStringValue;

    private String metricName;

    private String operatorName;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public DeviceLog() {
    }

    public DeviceLog(Device device, String eventType, Double oldNumericValue, Double newNumericValue, String metricName) {
        this.device = device;
        this.eventType = eventType;
        this.metricName = metricName;
        this.oldNumericValue = oldNumericValue;
        this.newNumericValue = newNumericValue;
    }

    public DeviceLog(Device device, String eventType, String oldStringValue, String newStringValue, String metricName) {
        this.device = device;
        this.eventType = eventType;
        this.metricName = metricName;
        this.oldStringValue = oldStringValue;
        this.newStringValue = newStringValue;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOldNumericValue() {
        return oldNumericValue;
    }

    public void setOldNumericValue(Double oldNumericValue) {
        this.oldNumericValue = oldNumericValue;
    }

    public Double getNewNumericValue() {
        return newNumericValue;
    }

    public void setNewNumericValue(Double newNumericValue) {
        this.newNumericValue = newNumericValue;
    }

    public String getOldStringValue() {
        return oldStringValue;
    }

    public void setOldStringValue(String oldStringValue) {
        this.oldStringValue = oldStringValue;
    }

    public String getNewStringValue() {
        return newStringValue;
    }

    public void setNewStringValue(String newStringValue) {
        this.newStringValue = newStringValue;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}