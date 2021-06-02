package com.model;

import java.util.ArrayList;

public class ModalVariant {
    private String variant_id;
    private String event_variant_id;
    private String requirement_variant_id;
    private String claim_quantity_id;
    private String shipped_variant_id;
    private boolean isChecked;
    private String variant_name;
    private String variant_price;
    private String rate_type;
    private String variant_color_str;
    private ArrayList<String> variant_color;
    private ArrayList<String> selectedVariantColor = new ArrayList<>();
    private ArrayList<VariantSite> variantSites;
    private String cod_price = "0";
    private String prepaid_price = "0";
    private String payfail_price = "0";
    private String otp_verify = "0";

    private String cod_quantity = "0";
    private String prepaid_quantity = "0";
    private String otp_quantity = "0";
    private String payfail_quantity = "0";

    private String shipped_cod_quantity = "";
    private String shipped_prepaid_quantity = "";
    private String shipped_payfail_quantity = "";
    private String shipped_otp_verify_quantity = "";

    private boolean isSelectedForVariant;
    private String shipping_quantity = "";
    private String selected_color = "";
    private String advance_paid = "";
    private String payment_mode = "";

//    private String claim_confirm_id;
//    private PayFailData payFailData;


    public String getEvent_variant_id() {
        return event_variant_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setEvent_variant_id(String event_variant_id) {
        this.event_variant_id = event_variant_id;
    }

    public ArrayList<VariantSite> getVariantSites() {
        return variantSites;
    }

    public void setVariantSites(ArrayList<VariantSite> variantSites) {
        this.variantSites = variantSites;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getShipped_variant_id() {
        return shipped_variant_id;
    }

    public void setShipped_variant_id(String shipped_variant_id) {
        this.shipped_variant_id = shipped_variant_id;
    }

    public String getShipped_cod_quantity() {
        return shipped_cod_quantity;
    }

    public void setShipped_cod_quantity(String shipped_cod_quantity) {
        this.shipped_cod_quantity = shipped_cod_quantity;
    }

    public String getShipped_prepaid_quantity() {
        return shipped_prepaid_quantity;
    }

    public void setShipped_prepaid_quantity(String shipped_prepaid_quantity) {
        this.shipped_prepaid_quantity = shipped_prepaid_quantity;
    }

    public String getShipped_payfail_quantity() {
        return shipped_payfail_quantity;
    }

    public void setShipped_payfail_quantity(String shipped_payfail_quantity) {
        this.shipped_payfail_quantity = shipped_payfail_quantity;
    }

    public String getShipped_otp_verify_quantity() {
        return shipped_otp_verify_quantity;
    }

    public void setShipped_otp_verify_quantity(String shipped_otp_verify_quantity) {
        this.shipped_otp_verify_quantity = shipped_otp_verify_quantity;
    }

    public String getShipping_quantity() {
        return shipping_quantity;
    }

    public void setShipping_quantity(String shipping_quantity) {
        this.shipping_quantity = shipping_quantity;
    }

    public String getSelected_color() {
        return selected_color;
    }

    public void setSelected_color(String selected_color) {
        this.selected_color = selected_color;
    }

    public String getAdvance_paid() {
        return advance_paid;
    }

    public void setAdvance_paid(String advance_paid) {
        this.advance_paid = advance_paid;
    }

    public String getRequirement_variant_id() {
        return requirement_variant_id;
    }

    public void setRequirement_variant_id(String requirement_variant_id) {
        this.requirement_variant_id = requirement_variant_id;
    }

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public String getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }

    public String getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(String variant_price) {
        this.variant_price = variant_price;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public String getVariant_color_str() {
        return variant_color_str;
    }

    public void setVariant_color_str(String variant_color_str) {
        this.variant_color_str = variant_color_str;
    }

    public ArrayList<String> getVariant_color() {
        return variant_color;
    }

    public void setVariant_color(ArrayList<String> variant_color) {
        this.variant_color = variant_color;
    }

    public ArrayList<String> getSelectedVariantColor() {
        return selectedVariantColor;
    }

    public void setSelectedVariantColor(ArrayList<String> selectedVariantColor) {
        this.selectedVariantColor = selectedVariantColor;
    }

    public boolean isSelectedForVariant() {
        return isSelectedForVariant;
    }

    public void setSelectedForVariant(boolean selectedForVariant) {
        isSelectedForVariant = selectedForVariant;
    }

    public String getCod_price() {
        return cod_price;
    }

    public void setCod_price(String cod_price) {
        this.cod_price = cod_price;
    }

    public String getPrepaid_price() {
        return prepaid_price;
    }

    public void setPrepaid_price(String prepaid_price) {
        this.prepaid_price = prepaid_price;
    }

    public String getPayfail_price() {
        return payfail_price;
    }

    public void setPayfail_price(String payfail_price) {
        this.payfail_price = payfail_price;
    }

    public String getOtp_verify() {
        return otp_verify;
    }

    public void setOtp_verify(String otp_verify) {
        this.otp_verify = otp_verify;
    }

    public String getClaim_quantity_id() {
        return claim_quantity_id;
    }

    public void setClaim_quantity_id(String claim_quantity_id) {
        this.claim_quantity_id = claim_quantity_id;
    }

    public String getCod_quantity() {
        return cod_quantity;
    }

    public void setCod_quantity(String cod_quantity) {
        this.cod_quantity = cod_quantity;
    }

    public String getPrepaid_quantity() {
        return prepaid_quantity;
    }

    public void setPrepaid_quantity(String prepaid_quantity) {
        this.prepaid_quantity = prepaid_quantity;
    }

    public String getOtp_quantity() {
        return otp_quantity;
    }

    public void setOtp_quantity(String otp_quantity) {
        this.otp_quantity = otp_quantity;
    }

    public String getPayfail_quantity() {
        return payfail_quantity;
    }

    public void setPayfail_quantity(String payfail_quantity) {
        this.payfail_quantity = payfail_quantity;
    }
}
