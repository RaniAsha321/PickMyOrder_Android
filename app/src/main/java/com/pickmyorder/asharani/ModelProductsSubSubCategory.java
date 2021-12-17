
package com.pickmyorder.asharani;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pickmyorder.asharani.SuperSubCategory;

public class ModelProductsSubSubCategory {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("Super_Sub_Category")
    @Expose
    private List<SuperSubCategory> superSubCategory = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<SuperSubCategory> getSuperSubCategory() {
        return superSubCategory;
    }

    public void setSuperSubCategory(List<SuperSubCategory> superSubCategory) {
        this.superSubCategory = superSubCategory;
    }

}
