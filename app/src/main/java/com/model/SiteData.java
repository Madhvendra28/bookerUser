package com.model;

import java.util.ArrayList;

public class SiteData {
    private String site_name;
    private String image;
    private boolean checked = false;

    private String total_quantity = "";
    private String claim_confirm_id;

    private ArrayList<ModalVariant> modalVariantArrayList;
    private PayFailData payFailData;


    public String getClaim_confirm_id() {
        return claim_confirm_id;
    }

    public void setClaim_confirm_id(String claim_confirm_id) {
        this.claim_confirm_id = claim_confirm_id;
    }

    public PayFailData getPayFailData() {
        return payFailData;
    }

    public void setPayFailData(PayFailData payFailData) {
        this.payFailData = payFailData;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ArrayList<ModalVariant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(ArrayList<ModalVariant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
    }
}
