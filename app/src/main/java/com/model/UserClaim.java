package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserClaim implements Parcelable {
    private String requirement_id;
    private String event_id;
    private String dealer_id;
    private String dealer_name;
    private String event_name;
    private String site_name;
    private String sale_type;
    private String start_date;
    private String start_timing;
    private String end_date;
    private String end_timing;
    private String model_name;
    private String required_quantity;
    private String claim_quantity;
    private String can_pay;
    private String can_pay_left;
    private String payment_on;
    private String rto;
    private String rto_charges;
    private String is_live;
    private String precautions;
    private String note;
    private String create_date;

    private ArrayList<SiteData> siteDataArrayList;
    private ArrayList<ModalVariant> modalVariantArrayList;
    private ArrayList<Address> addressArrayList;
    private ArrayList<TextLink> textLinkArrayList;

    private String claim_requirement_id;
    private String payment_method;
    private String is_pay_for_other;
    private String pay_amount;
    private String status;
    private String quantity;
    private String confirmed_quantity;
    private String failed_quantity;
    private String fail_quantity_reason;

    public UserClaim() {
        super();
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

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getSite_name() {
        return site_name;
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

    public String getCan_pay_left() {
        return can_pay_left;
    }

    public void setCan_pay_left(String can_pay_left) {
        this.can_pay_left = can_pay_left;
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

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public ArrayList<SiteData> getSiteDataArrayList() {
        return siteDataArrayList;
    }

    public void setSiteDataArrayList(ArrayList<SiteData> siteDataArrayList) {
        this.siteDataArrayList = siteDataArrayList;
    }

    public ArrayList<ModalVariant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(ArrayList<ModalVariant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
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

    public String getClaim_requirement_id() {
        return claim_requirement_id;
    }

    public void setClaim_requirement_id(String claim_requirement_id) {
        this.claim_requirement_id = claim_requirement_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getIs_pay_for_other() {
        return is_pay_for_other;
    }

    public void setIs_pay_for_other(String is_pay_for_other) {
        this.is_pay_for_other = is_pay_for_other;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getConfirmed_quantity() {
        return confirmed_quantity;
    }

    public void setConfirmed_quantity(String confirmed_quantity) {
        this.confirmed_quantity = confirmed_quantity;
    }

    public String getFailed_quantity() {
        return failed_quantity;
    }

    public void setFailed_quantity(String failed_quantity) {
        this.failed_quantity = failed_quantity;
    }

    public String getFail_quantity_reason() {
        return fail_quantity_reason;
    }

    public void setFail_quantity_reason(String fail_quantity_reason) {
        this.fail_quantity_reason = fail_quantity_reason;
    }

    public static Creator<UserClaim> getCREATOR() {
        return CREATOR;
    }

    protected UserClaim(Parcel in) {
        requirement_id = in.readString();
        event_id = in.readString();
        dealer_id = in.readString();
        dealer_name = in.readString();
        event_name = in.readString();
        site_name = in.readString();
        sale_type = in.readString();
        start_date = in.readString();
        start_timing = in.readString();
        end_date = in.readString();
        end_timing = in.readString();
        model_name = in.readString();
        required_quantity = in.readString();
        claim_quantity = in.readString();
        can_pay = in.readString();
        can_pay_left = in.readString();
        payment_on = in.readString();
        rto = in.readString();
        rto_charges = in.readString();
        is_live = in.readString();
        precautions = in.readString();
        note = in.readString();
        create_date = in.readString();
//        addressArrayList = in.createTypedArrayList(Address.CREATOR);
        claim_requirement_id = in.readString();
        payment_method = in.readString();
        is_pay_for_other = in.readString();
        pay_amount = in.readString();
        status = in.readString();
        quantity = in.readString();
        confirmed_quantity = in.readString();
        failed_quantity = in.readString();
        fail_quantity_reason = in.readString();
    }

    public static final Creator<UserClaim> CREATOR = new Creator<UserClaim>() {
        @Override
        public UserClaim createFromParcel(Parcel in) {
            return new UserClaim(in);
        }

        @Override
        public UserClaim[] newArray(int size) {
            return new UserClaim[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requirement_id);
        dest.writeString(event_id);
        dest.writeString(dealer_id);
        dest.writeString(dealer_name);
        dest.writeString(event_name);
        dest.writeString(site_name);
        dest.writeString(sale_type);
        dest.writeString(start_date);
        dest.writeString(start_timing);
        dest.writeString(end_date);
        dest.writeString(end_timing);
        dest.writeString(model_name);
        dest.writeString(required_quantity);
        dest.writeString(claim_quantity);
        dest.writeString(can_pay);
        dest.writeString(can_pay_left);
        dest.writeString(payment_on);
        dest.writeString(rto);
        dest.writeString(rto_charges);
        dest.writeString(is_live);
        dest.writeString(precautions);
        dest.writeString(note);
        dest.writeString(create_date);
        //dest.writeTypedList(addressArrayList);
        dest.writeString(claim_requirement_id);
        dest.writeString(payment_method);
        dest.writeString(is_pay_for_other);
        dest.writeString(pay_amount);
        dest.writeString(status);
        dest.writeString(quantity);
        dest.writeString(confirmed_quantity);
        dest.writeString(failed_quantity);
        dest.writeString(fail_quantity_reason);
    }
}
