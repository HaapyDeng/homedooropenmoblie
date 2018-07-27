package com.max_plus.homedooropenplate.Bean;

public class HouseBean {
    private int house_Id;
    private String house_Name;
    private boolean isChecked; //每条item的状态
    private int floor_num;

    public int getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(int floor_num) {
        this.floor_num = floor_num;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public HouseBean(int house_Id, String house_Name, int floor_num) {
        this.house_Id = house_Id;
        this.house_Name = house_Name;
        this.floor_num = floor_num;
    }

    public int getHouse_Id() {

        return house_Id;
    }

    public void setHouse_Id(int house_Id) {
        this.house_Id = house_Id;
    }

    public String getHouse_Name() {
        return house_Name;
    }

    public void setHouse_Name(String house_Name) {
        this.house_Name = house_Name;
    }


}
