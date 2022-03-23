
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBrands {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

}
