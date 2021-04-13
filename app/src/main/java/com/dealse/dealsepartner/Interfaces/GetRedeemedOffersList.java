package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Objects.GetOfferRequest;
import com.dealse.dealsepartner.Objects.GetOfferResponse;
import com.dealse.dealsepartner.Objects.GetUsedOfferList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRedeemedOffersList {

    @Headers("Content-Type:application/json")
    @POST("api/v1/HomeScreen/GetUserUsedOfferListByStore")
    Call<GetUsedOfferList> getRedeemedOffersList(@Header("Authorization") String authtoken, @Body GetOfferRequest getOfferRequest);
}
