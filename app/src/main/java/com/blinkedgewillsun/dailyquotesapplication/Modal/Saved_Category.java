package com.blinkedgewillsun.dailyquotesapplication.Modal;

public class Saved_Category {
    private int id ;
    private String catagory_name ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatagory_name() {
        return catagory_name;
    }

    public void setCatagory_name(String catagory_name) {
        this.catagory_name = catagory_name;
    }

    public Saved_Category(int id, String catagory_name) {
        this.id = id;
        this.catagory_name = catagory_name;
    }
}
