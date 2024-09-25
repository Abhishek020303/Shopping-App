package com.abhishek.onlineshop.Domain;

import com.abhishek.onlineshop.Fragment.ReviewFragment;

public class ReviewDomain {
    private String Name;
    private String Description;
    private String PicUrl;
    private double Rating;
    private int ItemId;
    public ReviewDomain(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}
