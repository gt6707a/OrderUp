package com.android.gt6707a.orderup;

public class MenuItem {

    private String name;
    private String description;
    private String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public MenuItem(String name, String description, String photo) {
        this.name = name;
        this.description = description;
        this.photo = photo;
    }
}
