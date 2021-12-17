
package com.pickmyorder.asharani;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cetalouge {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sectionid")
    @Expose
    private String sectionid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_to_display")
    @Expose
    private String imageToDisplay;
    @SerializedName("description")
    @Expose
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageToDisplay() {
        return imageToDisplay;
    }

    public void setImageToDisplay(String imageToDisplay) {
        this.imageToDisplay = imageToDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
