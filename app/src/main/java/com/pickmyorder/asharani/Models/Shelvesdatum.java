
package com.pickmyorder.asharani.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shelvesdatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("SectionImage")
    @Expose
    private String sectionImage;
    @SerializedName("cetalouge")
    @Expose
    private List<Cetalouge> cetalouge = null;

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

    public String getSectionImage() {
        return sectionImage;
    }

    public void setSectionImage(String sectionImage) {
        this.sectionImage = sectionImage;
    }

    public List<Cetalouge> getCetalouge() {
        return cetalouge;
    }

    public void setCetalouge(List<Cetalouge> cetalouge) {
        this.cetalouge = cetalouge;
    }

}
