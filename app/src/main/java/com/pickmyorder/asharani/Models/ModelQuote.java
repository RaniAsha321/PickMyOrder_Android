
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelQuote {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("QuotesData")
    @Expose
    private List<QuotesDatum> quotesData = null;
    @SerializedName("Number_of_awting")
    @Expose
    private Integer numberOfAwting;

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

    public List<QuotesDatum> getQuotesData() {
        return quotesData;
    }

    public void setQuotesData(List<QuotesDatum> quotesData) {
        this.quotesData = quotesData;
    }

    public Integer getNumberOfAwting() {
        return numberOfAwting;
    }

    public void setNumberOfAwting(Integer numberOfAwting) {
        this.numberOfAwting = numberOfAwting;
    }

}
