
package com.pickmyorder.asharani.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("bussiness_logo")
    @Expose
    private String bussiness_logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBussiness_logo() {
        return businessName;
    }

    public void setBussiness_logo(String businessName) {
        this.businessName = businessName;
    }

}
