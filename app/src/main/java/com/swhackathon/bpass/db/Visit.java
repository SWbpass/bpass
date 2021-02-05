package com.swhackathon.bpass.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Visit {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String storeName;
    private String storeNumber;
    private String adress;
    private String entryTime;
    private String exitTime;
    private Double longitude;
    private Double latitude;

    public Visit(String storeName, String storeNumber, String adress, String entryTime, String exitTime, double longitude, double latitude) {
        this.storeName = storeName;
        this.storeNumber = storeNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public String getAdress() {
        return adress;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
