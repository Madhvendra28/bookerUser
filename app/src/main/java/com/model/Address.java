package com.model;

import java.util.ArrayList;

public class Address {
    private String address_id;
    private ArrayList<String> site_nameList;
    private String payment_option;
    boolean isChecked;
    private String name;
    private String name_type;
    private String surname;
    private String surname_type;
    private String name_code;
    private String house_name;
    private String house_name_type;
    private String shop_tag;
    private String preposition;
    private String shop_name;
    private String shop_name_type;
    private String shop_name_or_surname_type;
    private String shop_surname;
    private String shop_surname_type;
    private String shop_tag_name;
    private String shop_tag_type;
    private String name_code_position;
    private String address_type;

    private String contact_no;
    private String contact_no_note;
    private String address;
    ArrayList<AddressList> addressLists;
    private String state;
    private String city;

    private String note;
    private String link;
    private String dealer_address_id;
    private String share_data;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getHouse_name_type() {
        return house_name_type;
    }

    public void setHouse_name_type(String house_name_type) {
        this.house_name_type = house_name_type;
    }

    public String getDealer_address_id() {
        return dealer_address_id;
    }

    public void setDealer_address_id(String dealer_address_id) {
        this.dealer_address_id = dealer_address_id;
    }

    public String getAddress_type() {
        return address_type;
    }

    public String getContact_no_note() {
        return contact_no_note;
    }

    public void setContact_no_note(String contact_no_note) {
        this.contact_no_note = contact_no_note;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getName_code_position() {
        return name_code_position;
    }

    public void setName_code_position(String name_code_position) {
        this.name_code_position = name_code_position;
    }

    private boolean isSelectedReqAdd;

    public Address() {
        super();
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }

    public String getSurname_type() {
        return surname_type;
    }

    public void setSurname_type(String surname_type) {
        this.surname_type = surname_type;
    }

    public String getShop_name_or_surname_type() {
        return shop_name_or_surname_type;
    }

    public void setShop_name_or_surname_type(String shop_name_or_surname_type) {
        this.shop_name_or_surname_type = shop_name_or_surname_type;
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

    public String getShop_tag_type() {
        return shop_tag_type;
    }

    public void setShop_tag_type(String shop_tag_type) {
        this.shop_tag_type = shop_tag_type;
    }

    public String getPreposition() {
        return preposition;
    }

    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public ArrayList<String> getSite_nameList() {
        return site_nameList;
    }

    public String getShop_surname() {
        return shop_surname;
    }

    public void setShop_surname(String shop_surname) {
        this.shop_surname = shop_surname;
    }

    public String getShop_tag_name() {
        return shop_tag_name;
    }

    public void setShop_tag_name(String shop_tag_name) {
        this.shop_tag_name = shop_tag_name;
    }

    public void setSite_nameList(ArrayList<String> site_nameList) {
        this.site_nameList = site_nameList;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName_code() {
        return name_code;
    }

    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    public String getShop_tag() {
        return shop_tag;
    }

    public void setShop_tag(String shop_tag) {
        this.shop_tag = shop_tag;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_name_type() {
        return shop_name_type;
    }

    public void setShop_name_type(String shop_name_type) {
        this.shop_name_type = shop_name_type;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<AddressList> getAddressLists() {
        return addressLists;
    }

    public void setAddressLists(ArrayList<AddressList> addressLists) {
        this.addressLists = addressLists;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSelectedReqAdd() {
        return isSelectedReqAdd;
    }

    public String getShop_surname_type() {
        return shop_surname_type;
    }

    public void setShop_surname_type(String shop_surname_type) {
        this.shop_surname_type = shop_surname_type;
    }

    public void setSelectedReqAdd(boolean selectedReqAdd) {
        isSelectedReqAdd = selectedReqAdd;
    }

    public String getShare_data() {
        return share_data;
    }

    public void setShare_data(String share_data) {
        this.share_data = share_data;
    }
}
