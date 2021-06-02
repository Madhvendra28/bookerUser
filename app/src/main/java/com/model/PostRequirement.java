package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostRequirement {
    private String requirement_id;
    private String event_id;
    private String dealer_id;
    private String event_name;
    private String site_name;
    private ArrayList<Site> sites;
    private List<SiteData> allsites;

    private String dealer_name;
    private String dealer_rating;
    private String sale_type;
    private String start_date;
    private String start_timing;
    private String end_date;
    private String end_timing;
    private String model_name;
    private String variant_name;
    private String required_quantity;
    private String claim_quantity;
    private String confirm_quantity;
    private String pay_fail_quantity;
    private String can_pay;
    private String payment_on;
    private String rto;
    private String rto_charges;
    private String is_live;
    private String event_id_data;

    private List<PojoModel> modelVariantList;

    private String precautions;
    private String note;
    private String share_data;
    private String posted_date;
    private String updated_date;
    private String bookkr_charges;
    private String total_bookker_charge;
    private String is_closed;
    private String is_deletable;

    private ArrayList<ModalVariant> modalVariantArrayList;
    private ArrayList<Address> addressArrayList;
    private ArrayList<TextLink> textLinkArrayList;

    private List<Address> addressList;
    private List<ModelPusAddress> pusAddressList;


    public List<SiteData> getAllsites() {
        return allsites;
    }

    public void setAllsites(List<SiteData> allsites) {
        this.allsites = allsites;
    }

    public String getIs_deletable() {
        return is_deletable;
    }

    public void setIs_deletable(String is_deletable) {
        this.is_deletable = is_deletable;
    }

    public String getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(String is_closed) {
        this.is_closed = is_closed;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public List<ModelPusAddress> getPusAddressList() {
        return pusAddressList;
    }

    public void setPusAddressList(List<ModelPusAddress> pusAddressList) {
        this.pusAddressList = pusAddressList;
    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
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

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public String getSite_name() {
        return site_name;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public String getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
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

    public String getCan_pay() {
        return can_pay;
    }

    public void setCan_pay(String can_pay) {
        this.can_pay = can_pay;
    }

    public String getPayment_on() {
        return payment_on;
    }

    public void setPayment_on(String payment_on) {
        this.payment_on = payment_on;
    }

    public String getRto() {
        return rto;
    }

    public void setRto(String rto) {
        this.rto = rto;
    }

    public String getRto_charges() {
        return rto_charges;
    }

    public void setRto_charges(String rto_charges) {
        this.rto_charges = rto_charges;
    }

    public ArrayList<ModalVariant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(ArrayList<ModalVariant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
    }

    public List<PojoModel> getModelVariantList() {
        return modelVariantList;
    }

    public void setModelVariantList(List<PojoModel> modelVariantList) {
        this.modelVariantList = modelVariantList;
    }

    public String getConfirm_quantity() {
        return confirm_quantity;
    }

    public void setConfirm_quantity(String confirm_quantity) {
        this.confirm_quantity = confirm_quantity;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Address> getAddressArrayList() {
        return addressArrayList;
    }

    public void setAddressArrayList(ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }

    public ArrayList<TextLink> getTextLinkArrayList() {
        return textLinkArrayList;
    }

    public void setTextLinkArrayList(ArrayList<TextLink> textLinkArrayList) {
        this.textLinkArrayList = textLinkArrayList;
    }

    public String getPay_fail_quantity() {
        return pay_fail_quantity;
    }

    public void setPay_fail_quantity(String pay_fail_quantity) {
        this.pay_fail_quantity = pay_fail_quantity;
    }

    public String getEvent_id_data() {
        return event_id_data;
    }

    public void setEvent_id_data(String event_id_data) {
        this.event_id_data = event_id_data;
    }

    public String getShare_data() {
        return share_data;
    }

    public void setShare_data(String share_data) {
        this.share_data = share_data;
    }

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getBookkr_charges() {
        return bookkr_charges;
    }

    public void setBookkr_charges(String bookkr_charges) {
        this.bookkr_charges = bookkr_charges;
    }

    public String getTotal_bookker_charge() {
        return total_bookker_charge;
    }

    public void setTotal_bookker_charge(String total_bookker_charge) {
        this.total_bookker_charge = total_bookker_charge;
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
}
