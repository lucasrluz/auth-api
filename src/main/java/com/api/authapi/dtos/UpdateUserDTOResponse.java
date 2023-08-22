package com.api.authapi.dtos;

public class UpdateUserDTOResponse {
    private String userId;

    public UpdateUserDTOResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
