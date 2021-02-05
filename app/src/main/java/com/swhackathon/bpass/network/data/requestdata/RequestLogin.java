package com.swhackathon.bpass.network.data.requestdata;

public class RequestLogin {

    private String id;
    private String password;
    private String role;

    public RequestLogin(String id, String password, String role){
        this.id = id;
        this.password = password;
        this.role = role;
    }


}
