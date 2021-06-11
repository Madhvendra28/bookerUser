
package com.mdModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantDatum {


    private Integer requirementVariantId;

    private Integer payFailVariantId;

    private String variantName;

    private Integer variantPrice;

    private String quantity;

    public Integer getRequirementVariantId() {
        return requirementVariantId;
    }

    public void setRequirementVariantId(Integer requirementVariantId) {
        this.requirementVariantId = requirementVariantId;
    }

    public Integer getPayFailVariantId() {
        return payFailVariantId;
    }

    public void setPayFailVariantId(Integer payFailVariantId) {
        this.payFailVariantId = payFailVariantId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Integer getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(Integer variantPrice) {
        this.variantPrice = variantPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
