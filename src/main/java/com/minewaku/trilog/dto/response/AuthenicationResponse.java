package com.minewaku.trilog.dto.response;

public class AuthenicationResponse {

    private String token;

    public AuthenicationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
