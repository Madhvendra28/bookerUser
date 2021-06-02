package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ShipOrderDetails implements Parcelable {
    private String shipping_details_id;
    private String create_date;
    private String dealer_name;
    private String requirement_id;
    private String claim_confirm_id;
    private String expected_date;
    private String site_name;
    private String name_on_order;
    private String model;
    private String order_value;
    private String tracking_id;
    private String courier;
    private String tracking_link;
    private String address;
    private String address_image;
    private String secret_note;
    private String status;
    private String payment_mode;
    private String courier_boy_no;
    private String pin;
    private String otp_for_delivery;
    private String comment;
    private String is_online_pay;
    private ArrayList<ModalVariant> modalVariantArrayList;

    public ShipOrderDetails() {
        super();
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getClaim_confirm_id() {
        return claim_confirm_id;
    }

    public void setClaim_confirm_id(String claim_confirm_id) {
        this.claim_confirm_id = claim_confirm_id;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getName_on_order() {
        return name_on_order;
    }

    public void setName_on_order(String name_on_order) {
        this.name_on_order = name_on_order;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public String getTracking_id() {
        return tracking_id;
    }

    public void setTracking_id(String tracking_id) {
        this.tracking_id = tracking_id;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getTracking_link() {
        return tracking_link;
    }

    public void setTracking_link(String tracking_link) {
        this.tracking_link = tracking_link;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_image() {
        return address_image;
    }

    public void setAddress_image(String address_image) {
        this.address_image = address_image;
    }

    public String getSecret_note() {
        return secret_note;
    }

    public void setSecret_note(String secret_note) {
        this.secret_note = secret_note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getCourier_boy_no() {
        return courier_boy_no;
    }

    public void setCourier_boy_no(String courier_boy_no) {
        this.courier_boy_no = courier_boy_no;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOtp_for_delivery() {
        return otp_for_delivery;
    }

    public void setOtp_for_delivery(String otp_for_delivery) {
        this.otp_for_delivery = otp_for_delivery;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIs_online_pay() {
        return is_online_pay;
    }

    public void setIs_online_pay(String is_online_pay) {
        this.is_online_pay = is_online_pay;
    }

    public ArrayList<ModalVariant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(ArrayList<ModalVariant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
    }

    public static Creator<ShipOrderDetails> getCREATOR() {
        return CREATOR;
    }

    protected ShipOrderDetails(Parcel in) {
        shipping_details_id = in.readString();
        create_date = in.readString();
        dealer_name = in.readString();
        requirement_id = in.readString();
        claim_confirm_id = in.readString();
        expected_date = in.readString();
        site_name = in.readString();
        name_on_order = in.readString();
        model = in.readString();
        order_value = in.readString();
        tracking_id = in.readString();
        courier = in.readString();
        tracking_link = in.readString();
        address = in.readString();
        address_image = in.readString();
        secret_note = in.readString();
        status = in.readString();
        payment_mode = in.readString();
        courier_boy_no = in.readString();
        pin = in.readString();
        otp_for_delivery = in.readString();
        comment = in.readString();
        is_online_pay = in.readString();
    }

    public static final Creator<ShipOrderDetails> CREATOR = new Creator<ShipOrderDetails>() {
        @Override
        public ShipOrderDetails createFromParcel(Parcel in) {
            return new ShipOrderDetails(in);
        }

        @Override
        public ShipOrderDetails[] newArray(int size) {
            return new ShipOrderDetails[size];
        }
    };

    public String getShipping_details_id() {
        return shipping_details_id;
    }

    public void setShipping_details_id(String shipping_details_id) {
        this.shipping_details_id = shipping_details_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipping_details_id);
        dest.writeString(create_date);
        dest.writeString(dealer_name);
        dest.writeString(requirement_id);
        dest.writeString(claim_confirm_id);
        dest.writeString(expected_date);
        dest.writeString(site_name);
        dest.writeString(name_on_order);
        dest.writeString(model);
        dest.writeString(order_value);
        dest.writeString(tracking_id);
        dest.writeString(courier);
        dest.writeString(tracking_link);
        dest.writeString(address);
        dest.writeString(address_image);
        dest.writeString(secret_note);
        dest.writeString(status);
        dest.writeString(payment_mode);
        dest.writeString(courier_boy_no);
        dest.writeString(pin);
        dest.writeString(otp_for_delivery);
        dest.writeString(comment);
        dest.writeString(is_online_pay);
    }
}
