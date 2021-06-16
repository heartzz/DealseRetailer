package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Objects.CreateOfferResponse;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.ScanOfferObject;
import com.dealse.dealsepartner.Objects.ScanOfferResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ScanUserOffer {
    @Headers("Content-Type:application/json")
    @POST("api/v1/Store/ScanUserOfferForStoreApp")
    Call<ScanOfferResponse> scanUserOffer(@Header("Authorization") String authtoken, @Body ScanOfferObject scanOfferObject);
}
