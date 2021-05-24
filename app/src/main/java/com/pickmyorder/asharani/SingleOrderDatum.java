
package com.pickmyorder.asharani;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleOrderDatum {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("variation_option_name")
    @Expose
    private String variationOptionName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("linenumber")
    @Expose
    private String linenumber;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pending_item")
    @Expose
    private Integer pendingItem;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariationOptionName() {
        return variationOptionName;
    }

    public void setVariationOptionName(String variationOptionName) {
        this.variationOptionName = variationOptionName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(String linenumber) {
        this.linenumber = linenumber;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getPendingItem() {
        return pendingItem;
    }

    public void setPendingItem(Integer pendingItem) {
        this.pendingItem = pendingItem;
    }

}
