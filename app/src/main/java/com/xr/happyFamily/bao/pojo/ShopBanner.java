package com.xr.happyFamily.bao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShopBanner {
    @Id(autoincrement = true)
    private Long id;

    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1757295000)
    public ShopBanner(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    @Generated(hash = 2131271615)
    public ShopBanner() {
    }

    
}
