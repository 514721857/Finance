package com.finance.ymt.sgr.finance.model;

/**
 * Data：2018/6/20/020-11:20
 * By  沈国荣
 * Description:
 */
public class RequestOrder {
    int status;
    int currPage;
    int pageSize;
    String address;

    int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
