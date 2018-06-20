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




    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }




    public Room(int imgId) {

        this.imgId = imgId;

    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    

    @Generated(hash = 860491580)
    public Room(Long roomId, String roomName, int houseId, String roomType, int imgId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.houseId = houseId;
        this.roomType = roomType;
        this.imgId = imgId;
    }

    @Generated(hash = 703125385)
    public Room() {
    }
}
