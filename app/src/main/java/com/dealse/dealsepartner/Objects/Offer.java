package com.dealse.dealsepartner.Objects;

import java.util.ArrayList;

public class Offer {

    private int offerId;
    private String name;
    private String offerListImage;
    private String effectiveDateRange;
    private String sortDescription;
    private boolean active;

    private int storeId;
    private String longDescription;
    private String termsAndConditions;
    private String startDate;
    private String endDate;
    private String addedDate;

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    private ArrayList<OfferImageListData> offerImagesLists;

    public ArrayList<OfferImageListData> getOfferImagesLists() {
        return offerImagesLists;
    }

    public void setOfferImagesLists(ArrayList<OfferImageListData> offerImagesLists) {
        this.offerImagesLists = offerImagesLists;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferListImage() {
        return offerListImage;
    }

    public void setOfferListImage(String offerListImage) {
        this.offerListImage = offerListImage;
    }

    public String getEffectiveDateRange() {
        return effectiveDateRange;
    }

    public void setEffectiveDateRange(String effectiveDateRange) {
        this.effectiveDateRange = effectiveDateRange;
    }

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
