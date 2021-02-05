package com.swhackathon.bpass.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestToServer {

    private final static String BASE_URL = "https://b-pass.herokuapp.com"; // Server
//    private final static String BASE_URL = "http://218.38.215.158:8080"; // Local
    private final static String Naver_map = "https://naveropenapi.apigw.ntruss.com";
    private static Retrofit retrofit = null;
    private static Retrofit naver_map = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 요청을 보낼 base url을 설정한다.
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 GsonConverterFactory를 추가한다.
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getNaver_map() {
        if (naver_map == null) {
            naver_map = new Retrofit.Builder()
                    .baseUrl(Naver_map) // 요청을 보낼 base url을 설정한다.
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 GsonConverterFactory를 추가한다.
                    .build();
        }

        return naver_map;
    }

}
