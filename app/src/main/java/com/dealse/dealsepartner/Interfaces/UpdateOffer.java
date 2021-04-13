package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Objects.CreateOfferResponse;
import com.dealse.dealsepartner.Objects.Offer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateOffer {
    @Headers("Content-Type:application/json")
    @POST("api/v1/Offer/UpdateOffer")
    Call<CreateOfferResponse> updateOffer(@Header("Authorization") String authtoken, @Body Offer offer);
}
