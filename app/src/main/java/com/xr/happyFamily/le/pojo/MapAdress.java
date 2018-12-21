package com.xr.happyFamily.le.pojo;

public class MapAdress {
    private int dsId;
    private String dsLongitude;
    private String dsLatitude;
    private long dsTime;
    private String lastTime;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getDsId() {
        return dsId;
    }

    public void setDsId(int dsId) {
        this.dsId = dsId;
    }

    public String getDsLongitude() {
        return dsLongitude;
    }

    public void setDsLongitude(String dsLongitude) {
        this.dsLongitude = dsLongitude;
    }

    public String getDsLatitude() {
        return dsLatitude;
    }

    public void setDsLatitude(String dsLatitude) {
        this.dsLatitude = dsLatitude;
    }

    public long getDsTime() {
        return dsTime;
    }

    public void setDsTime(long dsTime) {
        this.dsTime = dsTime;
    }
}
