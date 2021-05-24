package com.pickmyorder.asharani;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAccountsDetails {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
