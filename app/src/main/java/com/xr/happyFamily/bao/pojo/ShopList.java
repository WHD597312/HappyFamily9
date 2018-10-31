package com.xr.happyFamily.bao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Auto-generated: 2018-06-11 15:34:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
public class ShopList {


    @Id(autoincrement = false)
    private Long id;

    private String data;
   
    private String typeName;

    private int categoryId;

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

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

    @Generated(hash = 16521769)
    public ShopList(Long id, String data, String typeName, int categoryId) {
        this.id = id;
        this.data = data;
        this.typeName = typeName;
        this.categoryId = categoryId;
    }

    @Generated(hash = 360086818)
    public ShopList() {
    }

  




}



