package com.xr.happyFamily.bao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LeImage {
    @Id(autoincrement = true)
    private Long id;
    private int type;
    private String data;
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1631278796)
    public LeImage(Long id, int type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }
    @Generated(hash = 1502896064)
    public LeImage() {
    }


}
