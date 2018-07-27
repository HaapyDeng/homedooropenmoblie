package com.max_plus.homedooropenplate.Bean;

public class DanYuanBean {
    private int danyuan_Id;
    private String danyuan_Name;
    private boolean isChecked; //每条item的状态

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public DanYuanBean(int danyuan_Id, String danyuan_Name) {
        this.danyuan_Id = danyuan_Id;
        this.danyuan_Name = danyuan_Name;
    }

    public int getDanyuan_Id() {

        return danyuan_Id;
    }

    public void setDanyuan_Id(int danyuan_Id) {
        this.danyuan_Id = danyuan_Id;
    }

    public String getDanyuan_Name() {
        return danyuan_Name;
    }

    public void setDanyuan_Name(String danyuan_Name) {
        this.danyuan_Name = danyuan_Name;
    }


}
