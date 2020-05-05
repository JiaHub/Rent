package com.zhh.rent.sqlite;

import java.io.Serializable;

/**
 * Created by G40-70 on 2020/5/4.
 */

public class Rent implements Serializable {
    private Integer id;
    private String startTime;
    private String endTime;
    private String monthRent;
    private String rentWater;
    private String rentElectric;
    private String rentDays;
    private String rentResult;
    private String nowTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMonthRent() {
        return monthRent;
    }

    public void setMonthRent(String monthRent) {
        this.monthRent = monthRent;
    }

    public String getRentWater() {
        return rentWater;
    }

    public void setRentWater(String rentWater) {
        this.rentWater = rentWater;
    }

    public String getRentElectric() {
        return rentElectric;
    }

    public void setRentElectric(String rentElectric) {
        this.rentElectric = rentElectric;
    }

    public String getRentDays() {
        return rentDays;
    }

    public void setRentDays(String rentDays) {
        this.rentDays = rentDays;
    }

    public String getRentResult() {
        return rentResult;
    }

    public void setRentResult(String rentResult) {
        this.rentResult = rentResult;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", monthRent='" + monthRent + '\'' +
                ", rentWater='" + rentWater + '\'' +
                ", rentElectric='" + rentElectric + '\'' +
                ", rentDays='" + rentDays + '\'' +
                ", rentResult='" + rentResult + '\'' +
                ", nowTime='" + nowTime + '\'' +
                '}';
    }
}
