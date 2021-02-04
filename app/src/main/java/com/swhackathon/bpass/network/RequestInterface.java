package com.swhackathon.bpass.network;

import com.swhackathon.bpass.network.data.requestdata.RequestSignup;
import com.swhackathon.bpass.network.data.responsedata.ResponseAdressData;
import com.swhackathon.bpass.network.data.responsedata.ResponseSignup;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {

    // 회원가입
    @POST("/auth/signup")
    Call<ResponseSignup> userJoin(@Body RequestSignup data);

    // 로그인
    //@POST("/auth/signin")
    //Call<ResponseSignup> userJoin(@Body RequestSignup data);

    // Naver map Geocoding
    @GET("/map-geocode/v2/geocode")
    Call<ResponseAdressData> requestAdress(@HeaderMap Map<String, String> headers, @Query("query") String title);
}
