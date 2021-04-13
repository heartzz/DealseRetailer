package com.dealse.dealsepartner.Objects;

import com.dealse.dealsepartner.Interfaces.GetRedeemedOffersList;

import java.util.List;

public class GetUsedOfferList {

    private List<RedeemedOfeersResponse> data;
    private int code;
    private String message;

    public List<RedeemedOfeersResponse> getData() {
        return data;
    }

    public void setData(List<RedeemedOfeersResponse> data) {
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
