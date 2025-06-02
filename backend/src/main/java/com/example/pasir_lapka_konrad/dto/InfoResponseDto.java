package com.example.pasir_lapka_konrad.dto;

public class InfoResponseDto {
    public InfoResponseDto(String appName, String version, String message) {
        this.appName = appName;
        this.version = version;
        this.message = message;
    }

    private String appName;
    private String version;
    private String message;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
