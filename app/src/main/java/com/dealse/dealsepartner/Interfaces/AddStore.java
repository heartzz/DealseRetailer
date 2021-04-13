package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistRequest;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Entity.Store;
import com.dealse.dealsepartner.Objects.Partner;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddStore {


    @Multipart
    @POST("api/v1/Store/AddStore")
    Call<CheckStoreMobieNumberExistResponse> addStore(@Header("Authorization") String authtoken,
                                                      @Part("AreaId") Integer AreaId,
                                                      @Part("StoreTypeId") Integer StoreTypeId,
                                                      @Part("Name") RequestBody Name,
                                                      @Part("Email") RequestBody Email,
                                                      @Part("Address1") RequestBody Address1,
                                                      @Part("Latitude") double Latitude,
                                                      @Part("Longitude") double Longitude,
                                                      @Part("OwnerName") RequestBody OwnerName,
                                                      @Part("OwnerMobileNo") RequestBody OwnerMobileNo,
                                                      @Part("MobileNo1") RequestBody MobileNo1,
                                                      @Part MultipartBody.Part Logo
    );
}