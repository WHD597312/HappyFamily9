package com.xr.happyFamily.liwenchao.pojo;

public class Equipment {
    private String name;
    private String equipment;
    private String power;
    private int imgeId;

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setImgeId(int imgeId) {
        this.imgeId = imgeId;
    }

    public Equipment() {


    }
    public Equipment(String name, int imgeId) {
        this.name = name;
        this.imgeId = imgeId;
    }

    public String getName() {
        return name;
    }

    public int getImgeId() {
        return imgeId;
    }
}
