package com.android.gt6707a.orderup.entity;

public class OrderItem {
    public final static int WAITING = 1;
    public final static int READY = 2;

    private String id;
    private String item;
    private String customer;
    private long statusId;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
