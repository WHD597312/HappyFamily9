package com.xr.happyFamily.jia.pojo;

public class xnsb {
    private String name;
    private String equipment;
    private String power;
    private int imgeId;

    public xnsb(String name, String equipment, String power, int imgeId) {
        this.name = name;
        this.equipment = equipment;
        this.power = power;
        this.imgeId = imgeId;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setImgeId(int imgeId) {
        this.imgeId = imgeId;
    }

    public String getName() {

        return name;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getPower() {
        return power;
    }

    public int getImgeId() {
        return imgeId;
    }
}
