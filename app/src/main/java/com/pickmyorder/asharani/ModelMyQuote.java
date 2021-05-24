
package com.pickmyorder.asharani;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMyQuote {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("supplier_id")
    @Expose
    private String supplierId;
    @SerializedName("supplier_name")
    @Expose
    private String supplierName;
    @SerializedName("total_ex")
    @Expose
    private String totalEx;
    @SerializedName("total_inc")
    @Expose
    private String totalInc;
    @SerializedName("SingleOrderData")
    @Expose
    private List<SingleOrderDatum> singleOrderData = null;

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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getTotalEx() {
        return totalEx;
    }

    public void setTotalEx(String totalEx) {
        this.totalEx = totalEx;
    }

    public String getTotalInc() {
        return totalInc;
    }

    public void setTotalInc(String totalInc) {
        this.totalInc = totalInc;
    }

    public List<SingleOrderDatum> getSingleOrderData() {
        return singleOrderData;
    }

    public void setSingleOrderData(List<SingleOrderDatum> singleOrderData) {
        this.singleOrderData = singleOrderData;
    }

}
