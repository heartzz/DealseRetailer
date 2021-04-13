package com.dealse.dealsepartner.Objects;

public class RedeemedOfeersResponse {

    private int userUsedOfferId;
    private String userName;
    private String userMobileNo;
    private String offerName;
    private String redeemDate;

    public int getUserUsedOfferId() {
        return userUsedOfferId;
    }

    public void setUserUsedOfferId(int userUsedOfferId) {
        this.userUsedOfferId = userUsedOfferId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }
}
