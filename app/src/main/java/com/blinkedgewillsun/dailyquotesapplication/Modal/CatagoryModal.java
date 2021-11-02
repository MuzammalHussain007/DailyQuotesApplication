package com.blinkedgewillsun.dailyquotesapplication.Modal;

public class CatagoryModal {
    private String id ,name , picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public CatagoryModal(String id, String name, String picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }
}
