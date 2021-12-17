
package com.pickmyorder.asharani;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pickmyorder.asharani.SuperSubSubCategory;

public class ModelSubSubSubCategory {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("Super_Sub_sub_Category")
    @Expose
    private List<SuperSubSubCategory> superSubSubCategory = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<SuperSubSubCategory> getSuperSubSubCategory() {
        return superSubSubCategory;
    }

    public void setSuperSubSubCategory(List<SuperSubSubCategory> superSubSubCategory) {
        this.superSubSubCategory = superSubSubCategory;
    }

}
