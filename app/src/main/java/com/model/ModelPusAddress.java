package com.model;

import java.util.ArrayList;
import java.util.List;

public class ModelPusAddress {
    private String address_id;
    private ArrayList<String> site_nameList;
    private String payment_option;

    boolean isChecked;
    private String name;
    private String name_type;
    private String surname;
    private String surname_type;
    private String name_code;
    private String contact_no;
    private String contact_no_note;
    private String address_type;
    private String dealer_address_id;
    private String note;
    private List<ModelPusStore> pus_address;
    private String share_data;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<String> getSite_nameList() {
        return site_nameList;
    }
    public String getSite_name() {
        String site_name = "";
        for (int i = 0; i < site_nameList.size(); i++) {
            if (i != site_nameList.size() - 1)
                site_name += site_nameList.get(i) + ", ";
            else
                site_name += site_nameList.get(i);
        }
        return site_name;
    }
    public void setSite_nameList(ArrayList<String> site_nameList) {
        this.site_nameList = site_nameList;
    }

    public String getDealer_address_id() {
        return dealer_address_id;
    }

    public void setDealer_address_id(String dealer_address_id) {
        this.dealer_address_id = dealer_address_id;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname_type() {
        return surname_type;
    }

    public void setSurname_type(String surname_type) {
        this.surname_type = surname_type;
    }

    public String getName_code() {
        return name_code;
    }

    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getContact_no_note() {
        return contact_no_note;
    }

    public void setContact_no_note(String contact_no_note) {
        this.contact_no_note = contact_no_note;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public List<ModelPusStore> getPus_address() {
        return pus_address;
    }

    public void setPus_address(List<ModelPusStore> pus_address) {
        this.pus_address = pus_address;
    }

    @Override
    public String toString() {
        return "ModelPusAddress{" +
                "address_id='" + address_id + '\'' +
                ", site_nameList=" + site_nameList +
                ", payment_option='" + payment_option + '\'' +
                ", name='" + name + '\'' +
                ", name_type='" + name_type + '\'' +
                ", surname='" + surname + '\'' +
                ", surname_type='" + surname_type + '\'' +
                ", name_code='" + name_code + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", contact_no_note='" + contact_no_note + '\'' +
                ", address_type='" + address_type + '\'' +
                ", pus_address=" + pus_address +
                '}';
    }

    public String getShare_data() {
        return share_data;
    }

    public void setShare_data(String share_data) {
        this.share_data = share_data;
    }
}
