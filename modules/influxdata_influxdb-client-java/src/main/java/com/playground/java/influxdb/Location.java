package com.playground.java.influxdb;

import java.time.Instant;

public class Location {

    private String deviceId;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;

    public Location() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Location{" +
            "deviceId='" + deviceId + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", timestamp=" + timestamp +
            '}';
    }
}
