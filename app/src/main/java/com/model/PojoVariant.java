package com.model;


import java.util.ArrayList;
import java.util.List;

public class PojoVariant {

    private String variant_name;
    private String variant_id;
    private String variant_price;
    private String variant_color;
    private String rate_type;
    private String cod_price;
    private String prepaid_price;
    private String payfail_price;
    private String otp_verify;
    private String requirement_variant_id;
    private ArrayList<VariantSite> variantSites;

    private List<PojoString> colors;

    boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

    public String getVariant_color() {
        return variant_color;
    }

    public void setVariant_color(String variant_color) {
        this.variant_color = variant_color;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
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

    public List<PojoString> getColors() {
        return colors;
    }

    public void setColors(List<PojoString> colors) {
        this.colors = colors;
    }

    public ArrayList<VariantSite> getVariantSites() {
        return variantSites;
    }

    public void setVariantSites(ArrayList<VariantSite> variantSites) {
        this.variantSites = variantSites;
    }
}