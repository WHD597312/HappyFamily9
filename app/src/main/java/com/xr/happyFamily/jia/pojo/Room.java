package com.xr.happyFamily.jia.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Room {

    @Id(autoincrement = false)
    private Long roomId;
    private String roomName;
    private int houseId;
    private String roomType;
    private int  imgId;
    private String imgAddress;
    public String getImgAddress() {
        return this.imgAddress;
    }
    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }
    public int getImgId() {
        return this.imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    public String getRoomType() {
        return this.roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public int getHouseId() {
        return this.houseId;
    }
    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }
    public String getRoomName() {
        return this.roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public Long getRoomId() {
        return this.roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    @Generated(hash = 1829528656)
    public Room(Long roomId, String roomName, int houseId, String roomType,
            int imgId, String imgAddress) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.houseId = houseId;
        this.roomType = roomType;
        this.imgId = imgId;
        this.imgAddress = imgAddress;
    }
    @Generated(hash = 703125385)
    public Room() {
    }


   
}
