package com.model.confirmclaim;

import com.model.ModalVariant;
import com.model.PayFailData;

import java.util.ArrayList;
import java.util.List;

public class MdSiteData {
    private String requirement_model_id;
    private String site_name;
    private String total_quantity = "";
    private String claim_confirm_id;

    private List<Variant> modalVariantArrayList;


    public MdSiteData(String requirement_model_id, String site_name, String total_quantity, String claim_confirm_id, List<Variant> modalVariantArrayList) {
        this.requirement_model_id = requirement_model_id;
        this.site_name = site_name;
        this.total_quantity = total_quantity;
        this.claim_confirm_id = claim_confirm_id;
        this.modalVariantArrayList = modalVariantArrayList;
    }

    public String getRequirement_model_id() {
        return requirement_model_id;
    }

    public void setRequirement_model_id(String requirement_model_id) {
        this.requirement_model_id = requirement_model_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getClaim_confirm_id() {
        return claim_confirm_id;
    }

    public void setClaim_confirm_id(String claim_confirm_id) {
        this.claim_confirm_id = claim_confirm_id;
    }

    public List<Variant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(List<Variant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
    }
}
