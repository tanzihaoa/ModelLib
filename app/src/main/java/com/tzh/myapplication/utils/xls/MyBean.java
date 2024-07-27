package com.tzh.myapplication.utils.xls;

public class MyBean {
    int id;//序号
    String weighValue;//大象体重
    String time;//称重时间
    String date;//称重日期

    public MyBean(int id,String weighValue,String time,String date){
        this.id = id;
        this.weighValue = weighValue;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeighValue() {
        return weighValue;
    }

    public void setWeighValue(String weighValue) {
        this.weighValue = weighValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
