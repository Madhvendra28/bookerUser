package com.model;

import java.util.ArrayList;
public class Event {
    private String event_id_data;
    private String event_id;
    private String event_name;
    private ArrayList<Site> sites;
    private String sale_type;
    private String start_date;
    private String end_date;
    private String model_name;
    private ArrayList<EventModelVarient> eventModelVarients;
    private ArrayList<EventModelVarient> globalModelVarients;
    private ArrayList<String> variant_color;
    private String is_offer;
    private String offer_title;
    private String offer_details;
    private String event_link;
    private ArrayList<String> event_image;
    private String bookkr_charges;
    private String total_requirement;
    private String event_note;
    private String create_date;
    private String update_date;
    private String share_date;

    public String getShare_date() {
        return share_date;
    }

    public void setShare_date(String share_date) {
        this.share_date = share_date;
    }

    public String getEvent_note() {
        return event_note;
    }

    public void setEvent_note(String event_note) {
        this.event_note = event_note;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getEvent_id_data() {
        return event_id_data;
    }

    public void setEvent_id_data(String event_id_data) {
        this.event_id_data = event_id_data;
    }

    public String getTotal_requirement() {
        return total_requirement;
    }

    public void setTotal_requirement(String total_requirement) {
        this.total_requirement = total_requirement;
    }

    public ArrayList<EventModelVarient> getGlobalModelVarients() {
        return globalModelVarients;
    }

    public void setGlobalModelVarients(ArrayList<EventModelVarient> globalModelVarients) {
        this.globalModelVarients = globalModelVarients;
    }

    public String getBookkr_charges() {
        return bookkr_charges;
    }

    public void setBookkr_charges(String bookkr_charges) {
        this.bookkr_charges = bookkr_charges;
    }


    public ArrayList<String> getVariant_color() {
        return variant_color;
    }

    public void setVariant_color(ArrayList<String> variant_color) {
        this.variant_color = variant_color;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
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

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public ArrayList<EventModelVarient> getEventModelVarients() {
        return eventModelVarients;
    }

    public void setEventModelVarients(ArrayList<EventModelVarient> eventModelVarients) {
        this.eventModelVarients = eventModelVarients;
    }

    public String getIs_offer() {
        return is_offer;
    }

    public void setIs_offer(String is_offer) {
        this.is_offer = is_offer;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(String offer_details) {
        this.offer_details = offer_details;
    }

    public String getEvent_link() {
        return event_link;
    }

    public void setEvent_link(String event_link) {
        this.event_link = event_link;
    }

    public ArrayList<String> getEvent_image() {
        return event_image;
    }

    public void setEvent_image(ArrayList<String> event_image) {
        this.event_image = event_image;
    }
}