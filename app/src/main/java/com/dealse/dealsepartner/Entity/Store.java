package com.dealse.dealsepartner.Entity;

public class Store {

    private int AreaId;
    private int StoreTypeId;
    private String Name;
    private String Email;
    private String Address;
    private double Latitude;
    private double Longitude;
    private String OwnerName;
    private String OwnerMobileNo;
    private String MobileNo1;
    private String MobileNo2;
    private String OldLogo;
    private String DeviceID;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getMobileNo2() {
        return MobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        MobileNo2 = mobileNo2;
    }

    public String getOldLogo() {
        return OldLogo;
    }

    public void setOldLogo(String oldLogo) {
        OldLogo = oldLogo;
    }

    private String AddedDate;

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getMobileNo1() {
        return MobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        MobileNo1 = mobileNo1;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public int getStoreTypeId() {
        return StoreTypeId;
    }

    public void setStoreTypeId(int storeTypeId) {
        StoreTypeId = storeTypeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getOwnerMobileNo() {
        return OwnerMobileNo;
    }

    public void setOwnerMobileNo(String ownerMobileNo) {
        OwnerMobileNo = ownerMobileNo;
    }
}
