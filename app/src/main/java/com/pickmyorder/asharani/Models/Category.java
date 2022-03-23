package com.pickmyorder.asharani.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("cat-id")
    @Expose
    private String catId;
    @SerializedName("cat-name")
    @Expose
    private String catName;
    @SerializedName("cat-image")
    @Expose
    private String catImage;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

}
