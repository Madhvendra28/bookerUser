
package com.model.payfailModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VariantDatum {

    @SerializedName("requirement_variant_id")
    @Expose
    private Integer requirementVariantId;
    @SerializedName("variant_id")
    @Expose
    private Integer variantId;
    @SerializedName("variant_name")
    @Expose
    private String variantName;
    @SerializedName("variant_price")
    @Expose
    private Integer variantPrice;

    @SerializedName("pay_fail_quantity")
    @Expose
    private String payFailQuantity;

    public Integer getRequirementVariantId() {
        return requirementVariantId;
    }

    public void setRequirementVariantId(Integer requirementVariantId) {
        this.requirementVariantId = requirementVariantId;
    }

    public String getPayFailQuantity() {
        return payFailQuantity;
    }

    public void setPayFailQuantity(String payFailQuantity) {
        this.payFailQuantity = payFailQuantity;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
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

}
