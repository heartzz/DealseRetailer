package com.dealse.dealsepartner.Entity;

import com.dealse.dealsepartner.Objects.AreaListModel;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.StoreTypeApiModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckStoreMobileData implements Serializable {


    private int storeId;
    private ArrayList<AreaListModel> areaListModel;
    private ArrayList<StoreTypeApiModel> storeTypeApiModel;

    private String logoUrl;
    private String oldLogo;
    private String areaId;
    private String storeTypeId;
    private String name;
    private String email;
    private String mobileNo1;
    private String address;
    private String latitude;
    private String longitude;
    private String ownerName;
    private String ownerMobileNo;
    private String about;
    private String addedDate;
    private String deviceID;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getOldLogo() {
        return oldLogo;
    }

    public void setOldLogo(String oldLogo) {
        this.oldLogo = oldLogo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(String storeTypeId) {
        this.storeTypeId = storeTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo1() {
        return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        this.mobileNo1 = mobileNo1;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobileNo() {
        return ownerMobileNo;
    }

    public void setOwnerMobileNo(String ownerMobileNo) {
        this.ownerMobileNo = ownerMobileNo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public ArrayList<AreaListModel> getAreaListModel() {
        return areaListModel;
    }

    public void setAreaListModel(ArrayList<AreaListModel> areaListModel) {
        this.areaListModel = areaListModel;
    }

    public ArrayList<StoreTypeApiModel> getStoreTypeApiModel() {
        return storeTypeApiModel;
    }

    public void setStoreTypeApiModel(ArrayList<StoreTypeApiModel> storeTypeApiModel) {
        this.storeTypeApiModel = storeTypeApiModel;
    }




}
