package com.dealse.dealsepartner.Entity;

public class CheckStoreMobieNumberExistResponse {

    private CheckStoreMobileData data;
    private String code;
    private String message;

    public CheckStoreMobileData getData() {
        return data;
    }

    public void setData(CheckStoreMobileData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
