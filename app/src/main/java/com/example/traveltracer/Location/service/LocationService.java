package com.example.traveltracer.Location.service;

import com.example.traveltracer.Location.Data.locationData;
import com.example.traveltracer.Member.Response.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationService {

    @POST("/CheckpointManger")
    Call<CommonResponse> CheckPointSave(@Body locationData CheckPointData);

}
