package com.example.traveltracer.global.config;

import com.example.traveltracer.Location.service.LocationService;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    //로컬 주소 연결
    private final static String BASE_URL = "http://10.0.2.2:8092";  //연결 주소 (실질적으로 현재 로컬 주소 ) 난중에 서버 연결 할 부분
    private static Retrofit retrofit = null;

    public RetrofitConfig() {
    }

    public static Retrofit getClient() {
        if(retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }



    //지도 내용 전송 부분에서 자꾸 에러가 떠서 새로 생성함
    public static LocationService getLocationService() {
        return getClient().create(LocationService.class);
    }

    private static class NullOnEmptyConverterFactory extends Converter.Factory {
    }
}
