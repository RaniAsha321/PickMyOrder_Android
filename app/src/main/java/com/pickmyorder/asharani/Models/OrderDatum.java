package com.pickmyorder.asharani.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDatum {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("po_reffrence")
    @Expose
    private String poReffrence;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("total_ex_vat")
    @Expose
    private String totalExVat;
    @SerializedName("total_inc_vat")
    @Expose
    private String totalIncVat;
    @SerializedName("Order_descrption")
    @Expose
    private String orderDescrption;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("vanstock")
    @Expose
    private String vanstock;
    @SerializedName("reorder")
    @Expose
    private String reorder;


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    public String getPoReffrence() {
        return poReffrence;
    }

    public void setPoReffrence(String poReffrence) {
        this.poReffrence = poReffrence;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalExVat() {
        return totalExVat;
    }

    public void setTotalExVat(String totalExVat) {
        this.totalExVat = totalExVat;
    }

    public String getTotalIncVat() {
        return totalIncVat;
    }

    public void setTotalIncVat(String totalIncVat) {
        this.totalIncVat = totalIncVat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDescrption() {
        return orderDescrption;
    }

    public void setOrderDescrption(String orderDescrption) {
        this.orderDescrption = orderDescrption;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getVanstock() {
        return vanstock;
    }

    public void setVanstock(String vanstock) {
        this.vanstock = vanstock;
    }

    public String getReorder() {
        return reorder;
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }




}
