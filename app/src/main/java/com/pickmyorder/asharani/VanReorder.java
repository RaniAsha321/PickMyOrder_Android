
package com.pickmyorder.asharani;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VanReorder {

    @SerializedName("productname")
    @Expose
    private String productname;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
