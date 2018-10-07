package com.android.gt6707a.orderup.entity;

import com.google.firebase.firestore.Exclude;

public class OrderItem {
  public static final int WAITING = 1;
  public static final int READY = 2;

  private String id;
  private String item;
  private String customer;
  private long statusId;
  private String token;
  private long orderTime;

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

  @Exclude
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public long getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(long orderTime) {
    this.orderTime = orderTime;
  }
}
