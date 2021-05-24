
package com.pickmyorder.asharani;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBuyQuote {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("orderid")
    @Expose
    private Integer orderid;
    @SerializedName("Limitstatus")
    @Expose
    private String limitstatus;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getLimitstatus() {
        return limitstatus;
    }

    public void setLimitstatus(String limitstatus) {
        this.limitstatus = limitstatus;
    }

}
