package com.swhackathon.bpass.network.data.requestdata;

public class RequestFirebaseToken {

    private String userId;
    private String token;

    public RequestFirebaseToken(String userId, String token){
        this.userId = userId;
        this.token = token;
    }

}
