package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UpdateStore {

    @Multipart
    @POST("api/v1/Store/UpdateStoreForStoreApp")
    Call<CheckStoreMobieNumberExistResponse> updateStore(@Header("Authorization") String authtoken,
                                                      @Part("StoreId") Integer StoreId,
                                                      @Part("AreaId") Integer AreaId,
                                                      @Part("StoreTypeId") Integer StoreTypeId,
                                                      @Part("Name") RequestBody Name,
                                                      @Part("Email") RequestBody Email,
                                                      @Part("Address") RequestBody Address1,
                                                      @Part("Latitude") double Latitude,
                                                      @Part("Longitude") double Longitude,
                                                      @Part("OwnerName") RequestBody OwnerName,
                                                      @Part("OwnerMobileNo") RequestBody OwnerMobileNo,
                                                         @Part("MobileNo1") RequestBody MobileNo1,
                                                         @Part("AddedDate") RequestBody AddedDate,
                                                         @Part("OldLogo") RequestBody OldLogo,
                                                         @Part("DeviceID") RequestBody DeviceID,
                                                      @Part MultipartBody.Part Logo
    );
}
