package com.dealse.dealsepartner.Entity;

public class Data {

    private String token;
    private int storeId;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data(String token) {
        this.token = token;
    }

}
