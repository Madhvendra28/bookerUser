
package com.model.confirmclaim;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Datum {

    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("site_name")
    @Expose
    private String siteName;
    @SerializedName("total_claim_confirm")
    @Expose
    private Integer totalClaimConfirm;
    @SerializedName("model")
    @Expose
    private List<Model> model = null;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getTotalClaimConfirm() {
        return totalClaimConfirm;
    }

    public void setTotalClaimConfirm(Integer totalClaimConfirm) {
        this.totalClaimConfirm = totalClaimConfirm;
    }

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }

}
