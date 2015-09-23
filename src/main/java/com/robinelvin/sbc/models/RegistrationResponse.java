package com.robinelvin.sbc.models;

/**
 * @author Robin Elvin
 */
public class RegistrationResponse {
    private final String status;

    public RegistrationResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
