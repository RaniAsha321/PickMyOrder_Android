
package com.pickmyorder.asharani.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBillingDetails {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("BillingAddress")
    @Expose
    private String billingAddress;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CompanyAddress")
    @Expose
    private String companyAddress;
    @SerializedName("CompanyCity")
    @Expose
    private String companyCity;
    @SerializedName("CompanyPostcode")
    @Expose
    private String companyPostcode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyPostcode() {
        return companyPostcode;
    }

    public void setCompanyPostcode(String companyPostcode) {
        this.companyPostcode = companyPostcode;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

}
