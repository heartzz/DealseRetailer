package com.dealse.dealsepartner.Entity;

public class CheckStoreMobieNumberExistRequest {

    private String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public CheckStoreMobieNumberExistRequest(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
