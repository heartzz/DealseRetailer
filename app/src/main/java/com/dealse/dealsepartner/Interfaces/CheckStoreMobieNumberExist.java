package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistRequest;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CheckStoreMobieNumberExist {

    @Headers("Content-Type:application/json")
    @POST("api/v1/Store/CheckStoreMobieNumberExist")
    Call<CheckStoreMobieNumberExistResponse> checkStoreMobileNumberExist(@Header("Authorization") String authtoken,@Body CheckStoreMobieNumberExistRequest checkStoreMobieNumberExistRequest);
}
