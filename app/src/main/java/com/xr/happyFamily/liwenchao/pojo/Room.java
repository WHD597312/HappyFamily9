package com.xr.happyFamily.liwenchao.pojo;

public class Room {

    private int imgId;
    private String Name;

    public Room(String name) {
        Name = name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {


        return Name;
    }

    public Room(int imgId) {

        this.imgId = imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }


}
