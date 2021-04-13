package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImage {
    @Multipart
    @POST("api/v1/OfferBanner/AddOfferImage")
    Call<CheckStoreMobieNumberExistResponse> addImage(@Header("Authorization") String authtoken,
                                                      @Part("OfferId") RequestBody OfferId,
                                                      @Part MultipartBody.Part OfferImage
    );
}