package com.xr.happyFamily.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by win7 on 2018/3/12.
 */

@Entity
public class DeviceChild implements Serializable{
    @Id(autoincrement = false)
    private Long id;
    private String deviceName;
    private String macAddress;
    private int img;


    public DeviceChild() {
    }

    public DeviceChild(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceChild(String deviceName, int img) {
        this.deviceName = deviceName;
        this.img = img;
    }

    public DeviceChild(Long id, String deviceName, int img, int direction, Long houseId,
                       int masterControllerUserId, int type, int isUnlock) {
        this.id = id;
        this.deviceName = deviceName;
        this.img = img;



    }

    @Generated(hash = 1340189086)
    public DeviceChild(Long id, String deviceName, String macAddress, int img) {
        this.id = id;
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.img = img;
    }


    public int getImg() {
        return this.img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
