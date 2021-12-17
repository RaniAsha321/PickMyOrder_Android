
package com.pickmyorder.asharani;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pickmyorder.asharani.Shelvesdatum;

public class ModelGetSections {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("Shelvesdata")
    @Expose
    private List<Shelvesdatum> shelvesdata = null;

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

    public List<Shelvesdatum> getShelvesdata() {
        return shelvesdata;
    }

    public void setShelvesdata(List<Shelvesdatum> shelvesdata) {
        this.shelvesdata = shelvesdata;
    }

}
