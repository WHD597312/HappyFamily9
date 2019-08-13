package com.xr.happyFamily.jia.pojo;

import java.io.Serializable;

public class TimerTask implements Serializable {
    private String deviceMac;
    private int open;
    private int hour;
    private int min;
    private int hour2;
    private int min2;
    private int temp;


    public TimerTask() {
    }


    public TimerTask(String deviceMac, int open, int hour, int min, int temp) {
        this.deviceMac = deviceMac;
        this.open = open;
        this.hour = hour;
        this.min = min;
        this.temp = temp;
    }

    public TimerTask(String deviceMac, int open, int hour, int min, int hour2, int min2, int temp) {
        this.deviceMac = deviceMac;
        this.open = open;
        this.hour = hour;
        this.min = min;
        this.hour2 = hour2;
        this.min2 = min2;
        this.temp = temp;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }



    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHour2() {
        return hour2;
    }

    public void setHour2(int hour2) {
        this.hour2 = hour2;
    }

    public int getMin2() {
        return min2;
    }

    public void setMin2(int min2) {
        this.min2 = min2;
    }
}
