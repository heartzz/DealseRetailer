package com.dealse.dealsepartner.Interfaces;

import com.dealse.dealsepartner.Entity.AuthRequest;
import com.dealse.dealsepartner.Entity.AuthResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetAuthToken {
    @Headers("Content-Type:application/json")
    @POST("api/v1/Authentication/GetToken")
    Call<AuthResponse> getAuthToken(@Body AuthRequest authRequest);
}