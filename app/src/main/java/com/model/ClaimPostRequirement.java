package com.model;

import java.util.ArrayList;
import java.util.List;

public class ClaimPostRequirement {
    private String requirement_id;
    private String claim_requirement_id;
    private String create_date;
    private String event_id;
    private String dealer_id;
    private String event_name;
    private String dealer_name;
    private String dealer_rating;

    private String site_name;
    private ArrayList<Site> sites;
    private List<SiteData> allsites;

    private String sale_type;
    private String start_date;
    private String start_timing;
    private String end_date;
    private String end_timing;
    private String model_name;
    private String variant_name;
    private String required_quantity;
    private String claim_quantity;
    private String total_claim_quantity;
    private String confirm_claim;
    private String status;

    public ClaimPostRequirement(String requirement_id, String claim_requirement_id, String create_date, String event_id, String dealer_id, String event_name, String dealer_name, String dealer_rating, String site_name, ArrayList<Site> sites, List<SiteData> allsites, String sale_type, String start_date, String start_timing, String end_date, String end_timing, String model_name, String variant_name, String required_quantity, String claim_quantity, String total_claim_quantity, String confirm_claim, String status) {
        this.requirement_id = requirement_id;
        this.claim_requirement_id = claim_requirement_id;
        this.create_date = create_date;
        this.event_id = event_id;
        this.dealer_id = dealer_id;
        this.event_name = event_name;
        this.dealer_name = dealer_name;
        this.dealer_rating = dealer_rating;
        this.site_name = site_name;
        this.sites = sites;
        this.allsites = allsites;
        this.sale_type = sale_type;
        this.start_date = start_date;
        this.start_timing = start_timing;
        this.end_date = end_date;
        this.end_timing = end_timing;
        this.model_name = model_name;
        this.variant_name = variant_name;
        this.required_quantity = required_quantity;
        this.claim_quantity = claim_quantity;
        this.total_claim_quantity = total_claim_quantity;
        this.confirm_claim = confirm_claim;
        this.status = status;
    }

    public ClaimPostRequirement() {
    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getClaim_requirement_id() {
        return claim_requirement_id;
    }

    public void setClaim_requirement_id(String claim_requirement_id) {
        this.claim_requirement_id = claim_requirement_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_rating() {
        return dealer_rating;
    }

    public void setDealer_rating(String dealer_rating) {
        this.dealer_rating = dealer_rating;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public List<SiteData> getAllsites() {
        return allsites;
    }

    public void setAllsites(List<SiteData> allsites) {
        this.allsites = allsites;
    }

    public String getSale_type() {
        return sale_type;
    }

    public void setSale_type(String sale_type) {
        this.sale_type = sale_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_timing() {
        return start_timing;
    }

    public void setStart_timing(String start_timing) {
        this.start_timing = start_timing;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_timing() {
        return end_timing;
    }

    public void setEnd_timing(String end_timing) {
        this.end_timing = end_timing;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }

    public String getRequired_quantity() {
        return required_quantity;
    }

    public void setRequired_quantity(String required_quantity) {
        this.required_quantity = required_quantity;
    }

    public String getClaim_quantity() {
        return claim_quantity;
    }

    public void setClaim_quantity(String claim_quantity) {
        this.claim_quantity = claim_quantity;
    }

    public String getTotal_claim_quantity() {
        return total_claim_quantity;
    }

    public void setTotal_claim_quantity(String total_claim_quantity) {
        this.total_claim_quantity = total_claim_quantity;
    }

    public String getConfirm_claim() {
        return confirm_claim;
    }

    public void setConfirm_claim(String confirm_claim) {
        this.confirm_claim = confirm_claim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
