
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shelf {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("Shelvesdata")
    @Expose
    private List<Shelvesdatum> shelvesdata = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<Shelvesdatum> getShelvesdata() {
        return shelvesdata;
    }

    public void setShelvesdata(List<Shelvesdatum> shelvesdata) {
        this.shelvesdata = shelvesdata;
    }

}
