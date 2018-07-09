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
    String lable;
    String Style;
    int flag;
    boolean open;
    public boolean getOpen() {
        return this.open;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getStyle() {
        return this.Style;
    }
    public void setStyle(String Style) {
        this.Style = Style;
    }
    public String getLable() {
        return this.lable;
    }
    public void setLable(String lable) {
        this.lable = lable;
    }
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
    @Generated(hash = 1101508372)
    public Time(Long Id, int hour, int minutes, String day, String lable,
            String Style, int flag, boolean open) {
        this.Id = Id;
        this.hour = hour;
        this.minutes = minutes;
        this.day = day;
        this.lable = lable;
        this.Style = Style;
        this.flag = flag;
        this.open = open;
    }
    @Generated(hash = 37380482)
    public Time() {
    }
   
   
    
}
