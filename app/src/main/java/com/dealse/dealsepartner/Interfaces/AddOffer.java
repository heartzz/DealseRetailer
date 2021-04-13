package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistRequest;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Objects.CreateOfferResponse;
import com.dealse.dealsepartner.Objects.Offer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AddOffer {
    @Headers("Content-Type:application/json")
    @POST("api/v1/Offer/AddOffer")
    Call<CreateOfferResponse> createOffer(@Header("Authorization") String authtoken, @Body Offer offer);
}
