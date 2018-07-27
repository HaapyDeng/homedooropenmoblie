package com.max_plus.homedooropenplate.Bean;

public class LouYuBean {
    private int louyu_Id;
    private String louyu_Name;
    private boolean isChecked; //每条item的状态

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public LouYuBean(int louyu_Id, String louyu_Name) {
        this.louyu_Id = louyu_Id;
        this.louyu_Name = louyu_Name;
    }

    public int getLouyu_Id() {

        return louyu_Id;
    }

    public void setLouyu_Id(int louyu_Id) {
        this.louyu_Id = louyu_Id;
    }

    public String getLouyu_Name() {
        return louyu_Name;
    }

    public void setLouyu_Name(String louyu_Name) {
        this.louyu_Name = louyu_Name;
    }


}
