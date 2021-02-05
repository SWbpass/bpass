package com.swhackathon.bpass.network.data.responsedata;

public class VisitListData {

    private int id;
    private VisitorData visitor;
    private StoreData stroe;
    private String entryTime;
    private String exitTime;

    public int getId() {
        return id;
    }

    public VisitorData getVisitor() {
        return visitor;
    }

    public StoreData getStroe() {
        return stroe;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }
}
