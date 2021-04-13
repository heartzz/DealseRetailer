package com.dealse.dealsepartner.Objects;

import com.dealse.dealsepartner.Entity.CheckStoreMobileData;

public class GetAreaAndStoreTypeList {

    private CheckStoreMobileData data;
    private int code;
    private String message;

    public CheckStoreMobileData getData() {
        return data;
    }

    public void setData(CheckStoreMobileData data) {
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
