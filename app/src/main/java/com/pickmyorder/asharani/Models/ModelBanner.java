
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBanner {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("premissioncarousel")
    @Expose
    private String premissioncarousel;
    @SerializedName("Carouseltime")
    @Expose
    private String carouseltime;
    @SerializedName("BannersArray")
    @Expose
    private List<BannersArray> bannersArray = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<BannersArray> getBannersArray() {
        return bannersArray;
    }

    public void setBannersArray(List<BannersArray> bannersArray) {
        this.bannersArray = bannersArray;
    }
    public String getPremissioncarousel() {
        return premissioncarousel;
    }

    public void setPremissioncarousel(String premissioncarousel) {
        this.premissioncarousel = premissioncarousel;
    }

    public String getCarouseltime() {
        return carouseltime;
    }

    public void setCarouseltime(String carouseltime) {
        this.carouseltime = carouseltime;
    }

}
