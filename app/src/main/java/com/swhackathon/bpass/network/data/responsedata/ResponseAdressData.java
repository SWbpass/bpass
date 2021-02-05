package com.swhackathon.bpass.network.data.responsedata;

import java.util.List;

public class ResponseAdressData {

    private String status;
    private MetaAdressData meta;
    private List<AdressList> addresses;

    public String getStatus() {
        return status;
    }

    public MetaAdressData getMeta() {
        return meta;
    }

    public List<AdressList> getAddresses() {
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
