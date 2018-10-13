package com.android.gt6707a.orderup.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MenuItem {

  private String uid;
  private String name;
  private String photo;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public MenuItem() {}

  public MenuItem(String uid, String name, String photo) {
    this.uid = uid;
    this.name = name;
    this.photo = photo;
  }
}
