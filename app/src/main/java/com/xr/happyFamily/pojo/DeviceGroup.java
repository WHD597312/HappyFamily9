package com.xr.happyFamily.pojo;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DeviceGroup implements Comparable<DeviceGroup>{
    @Id(autoincrement = false)
    private Long id;
    private int userId;
    private String header;
    private String footer;
    private int color;
    private int masterControllerDeviceId;
    private int externalSensorsId;
    private String layers;
    public DeviceGroup(){}

    public DeviceGroup(Long id, String header, String houseName, String location, int masterControllerDeviceId, int externalSensorsId, String layers) {
        this.id = id;
        this.header = header;
        this.masterControllerDeviceId = masterControllerDeviceId;
        this.externalSensorsId = externalSensorsId;
        this.layers = layers;
    }

    @Generated(hash = 1933139149)
    public DeviceGroup(Long id, int userId, String header, String footer, int color, int masterControllerDeviceId, int externalSensorsId, String layers) {
        this.id = id;
        this.userId = userId;
        this.header = header;
        this.footer = footer;
        this.color = color;
        this.masterControllerDeviceId = masterControllerDeviceId;
        this.externalSensorsId = externalSensorsId;
        this.layers = layers;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getMasterControllerDeviceId() {
        return masterControllerDeviceId;
    }

    public void setMasterControllerDeviceId(int masterControllerDeviceId) {
        this.masterControllerDeviceId = masterControllerDeviceId;
    }


    public int getExternalSensorsId() {
        return externalSensorsId;
    }

    public void setExternalSensorsId(int externalSensorsId) {
        this.externalSensorsId = externalSensorsId;
    }

    public String getLayers() {
        return layers;
    }

    public void setLayers(String layers) {
        this.layers = layers;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)
            return true;
        if (obj instanceof DeviceGroup){
            DeviceGroup deviceGroup= (DeviceGroup) obj;
            if (this.getId()==deviceGroup.getId())
                return true;
            else
                return false;

        }else
            return false;

    }

    @Override
    public int compareTo(@NonNull DeviceGroup o) {
        if (this.getId()>o.getId())
            return 1;
        else if (this.getId()==o.getId())
            return 0;
        return 0;
    }
}
