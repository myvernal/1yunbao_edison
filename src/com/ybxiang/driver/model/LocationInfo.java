package com.ybxiang.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/8/23.
 */
public class LocationInfo implements Serializable {

    private String address;//地址
    private String X;
    private String Y;
    private String result;//结果
    private String timestamp;
    private String BeginTime;
    private boolean done = false;//是否超时

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
