package com.example.android_business_card;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// S04M03-3 Add Model Object
public class BusinessCard implements Parcelable {
    private int id;
    private String category, name;

    public BusinessCard(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected BusinessCard(Parcel in) {
        id = in.readInt();
        category = in.readString();
        name = in.readString();
    }

    public static final Creator<BusinessCard> CREATOR = new Creator<BusinessCard>() {
        @Override
        public BusinessCard createFromParcel(Parcel in) {
            return new BusinessCard(in);
        }

        @Override
        public BusinessCard[] newArray(int size) {
            return new BusinessCard[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(name);
    }
}
