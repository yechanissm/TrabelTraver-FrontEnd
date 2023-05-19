package com.example.traveltracer.login.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private final static String BASE_URL = "http://10.0.2.2:8092";
    private static Retrofit retrofit = null;

    public RetrofitConfig() {
    }

    public static Retrofit getClient() {
        if(retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
