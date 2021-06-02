package com.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ANKIT on 08-Jul-17.
 */

public class UserProfile implements Parcelable {
    private String user_id;
    private String user_code;
    private String name;
    private String email_id;
    private String contact_no;
    private String gender;
    private String date_of_birth;
    private String address;
    private String area;
    private String state;
    private String city;
    private String postal_code;

    private String profileImage;
    private Bitmap profileImageBitmap;

    private String document_type;
    private String document_number;
    private String documentImage;
    private Bitmap documentImageBitmap;

    private String password;
    private String otp;
    private String referralCode;
    private String captcha;
    private String whatsappNumber;

    private String facebook_link;
    private String twitter_link;
    private String instagram_link;
    private String telegram_link;

    private String verification_status;
    private String status;
    private String wallet_amount;
    private String token;

    public UserProfile() {
        super();
    }

    protected UserProfile(Parcel in) {
        user_id = in.readString();
        user_code = in.readString();
        name = in.readString();
        email_id = in.readString();
        contact_no = in.readString();
        gender = in.readString();
        date_of_birth = in.readString();
        address = in.readString();
        area = in.readString();
        state = in.readString();
        city = in.readString();
        postal_code = in.readString();
        profileImage = in.readString();
        profileImageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        document_type = in.readString();
        document_number = in.readString();
        documentImage = in.readString();
        documentImageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        password = in.readString();
        otp = in.readString();
        referralCode = in.readString();
        captcha = in.readString();
        whatsappNumber = in.readString();
        facebook_link = in.readString();
        twitter_link = in.readString();
        instagram_link = in.readString();
        telegram_link = in.readString();
        verification_status = in.readString();
        status = in.readString();
        wallet_amount = in.readString();
        token = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Bitmap getProfileImageBitmap() {
        return profileImageBitmap;
    }

    public void setProfileImageBitmap(Bitmap profileImageBitmap) {
        this.profileImageBitmap = profileImageBitmap;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Bitmap getDocumentImageBitmap() {
        return documentImageBitmap;
    }

    public void setDocumentImageBitmap(Bitmap documentImageBitmap) {
        this.documentImageBitmap = documentImageBitmap;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getTelegram_link() {
        return telegram_link;
    }

    public void setTelegram_link(String telegram_link) {
        this.telegram_link = telegram_link;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_code);
        dest.writeString(name);
        dest.writeString(email_id);
        dest.writeString(contact_no);
        dest.writeString(gender);
        dest.writeString(date_of_birth);
        dest.writeString(address);
        dest.writeString(area);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(postal_code);
        dest.writeString(profileImage);
        dest.writeParcelable(profileImageBitmap, flags);
        dest.writeString(document_type);
        dest.writeString(document_number);
        dest.writeString(documentImage);
        dest.writeParcelable(documentImageBitmap, flags);
        dest.writeString(password);
        dest.writeString(otp);
        dest.writeString(referralCode);
        dest.writeString(captcha);
        dest.writeString(whatsappNumber);
        dest.writeString(facebook_link);
        dest.writeString(twitter_link);
        dest.writeString(instagram_link);
        dest.writeString(telegram_link);
        dest.writeString(verification_status);
        dest.writeString(status);
        dest.writeString(wallet_amount);
        dest.writeString(token);
    }
}
