package com.dealse.dealsepartner.Objects;

import java.io.Serializable;


/**
 * Created by harshank on 25/5/17.
 */

public class Partner implements Serializable {

    private String ownermobileNumber;
    private String OwnerName;
    private String emaiId;
    private String OwnerMobileNo;
    private String Address1;
    private double Latitude;
    private double Longitude;

    private int AreaId;
    private int StoreTypeId;
    private String Name;

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getOwnermobileNumber() {
        return ownermobileNumber;
    }

    public void setOwnermobileNumber(String ownermobileNumber) {
        this.ownermobileNumber = ownermobileNumber;
    }

    public String getEmaiId() {
        return emaiId;
    }

    public void setEmaiId(String emaiId) {
        this.emaiId = emaiId;
    }

    public String getOwnerMobileNo() {
        return OwnerMobileNo;
    }

    public void setOwnerMobileNo(String ownerMobileNo) {
        OwnerMobileNo = ownerMobileNo;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
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
}
