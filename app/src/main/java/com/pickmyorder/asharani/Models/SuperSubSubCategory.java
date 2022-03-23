
package com.pickmyorder.asharani.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuperSubSubCategory {

    @SerializedName("super_sub_sub_cat-id")
    @Expose
    private String superSubSubCatId;
    @SerializedName("super_sub_sub_cat-name")
    @Expose
    private String superSubSubCatName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getSuperSubSubCatId() {
        return superSubSubCatId;
    }

    public void setSuperSubSubCatId(String superSubSubCatId) {
        this.superSubSubCatId = superSubSubCatId;
    }

    public String getSuperSubSubCatName() {
        return superSubSubCatName;
    }

    public void setSuperSubSubCatName(String superSubSubCatName) {
        this.superSubSubCatName = superSubSubCatName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
