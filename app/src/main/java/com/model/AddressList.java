package com.model;

public class AddressList {
    private String area;
    private String colony_name;
    private String landmark;
    private String postal_code;
    private String address_data_id;

    public AddressList(String area, String colony_name, String landmark, String postal_code, String address_data_id) {
        this.area = area;
        this.colony_name = colony_name;
        this.landmark = landmark;
        this.postal_code = postal_code;
        this.address_data_id = address_data_id;
    }

    public String getAddress_data_id() {
        return address_data_id;
    }

    public void setAddress_data_id(String address_data_id) {
        this.address_data_id = address_data_id;
    }

    public AddressList() {
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getColony_name() {
        return colony_name;
    }

    public void setColony_name(String colony_name) {
        this.colony_name = colony_name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
