
package com.model.payfailModel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("site_logo")
    @Expose
    private String siteLogo;
    @SerializedName("dealer_can_pay")
    @Expose
    private Integer dealerCanPay;
    @SerializedName("left_slot")
    @Expose
    private Integer leftSlot;
    @SerializedName("model_data")
    @Expose
    private List<ModelDatum> modelData = null;

    public String getSiteLogo() {
        return siteLogo;
    }

    public void setSiteLogo(String siteLogo) {
        this.siteLogo = siteLogo;
    }

    public Integer getDealerCanPay() {
        return dealerCanPay;
    }

    public void setDealerCanPay(Integer dealerCanPay) {
        this.dealerCanPay = dealerCanPay;
    }

    public Integer getLeftSlot() {
        return leftSlot;
    }

    public void setLeftSlot(Integer leftSlot) {
        this.leftSlot = leftSlot;
    }

    public List<ModelDatum> getModelData() {
        return modelData;
    }

    public void setModelData(List<ModelDatum> modelData) {
        this.modelData = modelData;
    }

}
