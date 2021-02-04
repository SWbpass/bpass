package com.swhackathon.bpass.network.data.responsedata;

public class ResponseAdressData {

    private String status;
    private MetaAdressData meta;
    private AdressList addresses;

    public String getStatus() {
        return status;
    }

    public MetaAdressData getMeta() {
        return meta;
    }

    public AdressList getAddresses() {
        return addresses;
    }

}

class MetaAdressData {

    private int totalCount;
    private int page;
    private int count;

    public int getCount() {
        return count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }
}

class AdressList {

    private String roadAddress;
    private String jibunAddress;
    private Double x;
    private Double y;

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
