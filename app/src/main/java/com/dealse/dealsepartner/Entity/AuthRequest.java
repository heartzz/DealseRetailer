package com.dealse.dealsepartner.Entity;

public class AuthRequest {

    private String clientSecret;

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public AuthRequest(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
