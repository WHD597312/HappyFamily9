package com.xr.happyFamily.le.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class AppUsing {
    @Id(autoincrement = false)
    private int id;
    private String iconAdress;
//    private Mulit
    private String appName;
    private String appUseLastTime;
    private String useTime;
    private int appDerailId;



    @Generated(hash = 1587990480)
    public AppUsing(int id, String iconAdress, String appName,
            String appUseLastTime, String useTime, int appDerailId) {
        this.id = id;
        this.iconAdress = iconAdress;
        this.appName = appName;
        this.appUseLastTime = appUseLastTime;
        this.useTime = useTime;
        this.appDerailId = appDerailId;
    }

    @Generated(hash = 13684171)
    public AppUsing() {
    }



    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUseLastTime() {
        return appUseLastTime;
    }

    public void setAppUseLastTime(String appUseLastTime) {
        this.appUseLastTime = appUseLastTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public int getAppDerailId() {
        return appDerailId;
    }

    public void setAppDerailId(int appDerailId) {
        this.appDerailId = appDerailId;
    }

    public String getIconAdress() {
        return this.iconAdress;
    }

    public void setIconAdress(String iconAdress) {
        this.iconAdress = iconAdress;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
