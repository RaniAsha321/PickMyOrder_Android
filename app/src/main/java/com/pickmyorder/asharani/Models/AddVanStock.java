
package com.pickmyorder.asharani.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVanStock {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("VanStockReorder")
    @Expose
    private Boolean vanStockReorder;
    @SerializedName("order_complete")
    @Expose
    private Integer orderComplete;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getVanStockReorder() {
        return vanStockReorder;
    }

    public void setVanStockReorder(Boolean vanStockReorder) {
        this.vanStockReorder = vanStockReorder;
    }

    public Integer getOrderComplete() {
        return orderComplete;
    }

    public void setOrderComplete(Integer orderComplete) {
        this.orderComplete = orderComplete;
    }

}
