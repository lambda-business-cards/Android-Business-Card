package com.example.android_business_card;

import java.io.Serializable;

// S04M03-3 Add Model Object
public class BusinessCard implements Serializable {
    private int id;
    private String category, name;

    public BusinessCard(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }
}
