
package com.model.confirmclaim;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Variant {

    @SerializedName("requirement_variant_id")
    @Expose
    private Integer requirementVariantId;
    @SerializedName("variant_id")
    @Expose
    private Integer variantId;
    @SerializedName("variant_name")
    @Expose
    private String variantName;
    @SerializedName("cod")
    @Expose
    private Integer cod;
    @SerializedName("pre_paid")
    @Expose
    private Integer prePaid;
    @SerializedName("pay_fail")
    @Expose
    private Integer payFail;
    @SerializedName("otp_verify")
    @Expose
    private Integer otpVerify;

    public Integer getRequirementVariantId() {
        return requirementVariantId;
    }

    public void setRequirementVariantId(Integer requirementVariantId) {
        this.requirementVariantId = requirementVariantId;
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

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Integer getPrePaid() {
        return prePaid;
    }

    public void setPrePaid(Integer prePaid) {
        this.prePaid = prePaid;
    }

    public Integer getPayFail() {
        return payFail;
    }

    public void setPayFail(Integer payFail) {
        this.payFail = payFail;
    }

    public Integer getOtpVerify() {
        return otpVerify;
    }

    public void setOtpVerify(Integer otpVerify) {
        this.otpVerify = otpVerify;
    }

}
