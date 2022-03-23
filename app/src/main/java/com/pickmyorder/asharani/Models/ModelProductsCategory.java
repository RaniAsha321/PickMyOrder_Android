
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelProductsCategory {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("Category")
    @Expose
    private List<Category> category = null;

    @SerializedName("numberofawating")
    @Expose
    private Integer numberofawating;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Integer getNumberofawating() {
        return numberofawating;
    }

    public void setNumberofawating(Integer numberofawating) {
        this.numberofawating = numberofawating;
    }

}
