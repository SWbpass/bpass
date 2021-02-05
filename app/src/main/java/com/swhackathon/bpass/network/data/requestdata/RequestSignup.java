package com.swhackathon.bpass.network.data.requestdata;

public class RequestSignup {

    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private String role;
    private String storePhoneNumber;
    private String storeName;
    private Double latitude;
    private Double longitude;

    public RequestSignup(String id, String password, String name, String phoneNumber, String address, String role, String storePhoneNumber, String storeName, Double latitude, Double longitude){
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.storePhoneNumber = storePhoneNumber;
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RequestSignup(String id, String password, String name, String phoneNumber, String address, String role){
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }


}
