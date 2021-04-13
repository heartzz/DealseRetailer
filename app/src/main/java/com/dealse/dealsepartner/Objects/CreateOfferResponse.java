package com.dealse.dealsepartner.Objects;

import com.dealse.dealsepartner.Entity.CheckStoreMobileData;

public class CreateOfferResponse {
    private AddOfferData data;
    private int code;
    private String message;

    public AddOfferData getData() {
        return data;
    }

    public void setData(AddOfferData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
