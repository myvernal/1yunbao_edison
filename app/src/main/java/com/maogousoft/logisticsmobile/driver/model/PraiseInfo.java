package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2015/5/9.
 */
public class PraiseInfo implements Serializable {
    private int count;//点赞数
    private String IsEnablePraise;//是否可以点赞

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIsEnablePraise() {
        return IsEnablePraise;
    }

    public void setIsEnablePraise(String isEnablePraise) {
        IsEnablePraise = isEnablePraise;
    }
}
