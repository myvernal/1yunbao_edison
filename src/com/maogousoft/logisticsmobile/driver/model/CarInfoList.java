package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Edison on 2015/3/6.
 */
public class CarInfoList implements Serializable {

    private List<CarInfo> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int totalRow;

    public List<CarInfo> getList() {
        return list;
    }

    public void setList(List<CarInfo> list) {
        this.list = list;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}
