package com.xr.happyFamily.jia.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Hourse {

    @Id(autoincrement = false)
    private Long houseId;
    private String houseName;
    private String houseAddress;
    private int userId;


    @Generated(hash = 553284582)
    public Hourse(Long houseId, String houseName, String houseAddress, int userId) {
        this.houseId = houseId;
        this.houseName = houseName;
        this.houseAddress = houseAddress;
        this.userId = userId;
    }

    @Generated(hash = 199854596)
    public Hourse() {
    }


    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

  
}
