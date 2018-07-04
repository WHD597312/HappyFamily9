package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Time {
    @Id(autoincrement = true)
    Long Id;
    int hour;
    int minutes;
    String day;
    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public int getMinutes() {
        return this.minutes;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    public int getHour() {
        return this.hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    @Generated(hash = 1161402333)
    public Time(Long Id, int hour, int minutes, String day) {
        this.Id = Id;
        this.hour = hour;
        this.minutes = minutes;
        this.day = day;
    }
    @Generated(hash = 37380482)
    public Time() {
    }

    
}
