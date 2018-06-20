package com.xr.happyFamily.jia.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Room1 {
    @Id(autoincrement = false)
    private Long roomId;
    private String roomName;
    private int houseId;
    private String roomType;

    @Generated(hash = 819419166)
    public Room1(Long roomId, String roomName, int houseId, String roomType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.houseId = houseId;
        this.roomType = roomType;
    }

    @Generated(hash = 296051058)
    public Room1() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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
}
