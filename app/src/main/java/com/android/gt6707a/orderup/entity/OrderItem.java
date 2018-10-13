package com.android.gt6707a.orderup.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class OrderItem {
  public static final int WAITING = 1;
  public static final int READY = 2;

  private String key;
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
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
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

  @Exclude
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("item", item);
    result.put("customer", customer);
    result.put("statusId", statusId);
    result.put("key", key);
    result.put("token", token);
    result.put("orderTime", orderTime);

    return result;
  }
}
