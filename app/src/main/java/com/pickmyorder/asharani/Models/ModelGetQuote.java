
package com.pickmyorder.asharani.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetQuote {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("Quotesid")
    @Expose
    private Integer quotesid;
    @SerializedName("Limitstatus")
    @Expose
    private Integer limitstatus;
    @SerializedName("result")
    @Expose
    private String result;

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

    public Integer getQuotesid() {
        return quotesid;
    }

    public void setQuotesid(Integer quotesid) {
        this.quotesid = quotesid;
    }

    public Integer getLimitstatus() {
        return limitstatus;
    }

    public void setLimitstatus(Integer limitstatus) {
        this.limitstatus = limitstatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
