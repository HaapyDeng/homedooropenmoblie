package com.max_plus.homedooropenplate.Bean;

public class XiaoQuBean {
    private int xiaoqu_Id;
    private String xiaoqu_Name;
    private boolean isChecked; //每条item的状态

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public XiaoQuBean(int xiaoqu_Id, String xiaoqu_Name) {
        this.xiaoqu_Id = xiaoqu_Id;
        this.xiaoqu_Name = xiaoqu_Name;
    }

    public int getXiaoqu_Id() {

        return xiaoqu_Id;
    }

    public void setXiaoqu_Id(int xiaoqu_Id) {
        this.xiaoqu_Id = xiaoqu_Id;
    }

    public String getXiaoqu_Name() {
        return xiaoqu_Name;
    }

    public void setXiaoqu_Name(String xiaoqu_Name) {
        this.xiaoqu_Name = xiaoqu_Name;
    }


}
