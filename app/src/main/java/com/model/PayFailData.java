package com.model;

public class PayFailData {
    private String payfail_details_id;
    private String username;
    private String password;
    private String otp_send_on;
    private String whatsapp_no;
    private String no_of_orders;
    private String total_amount;
    private String time_left;
    private String is_cod_available;

    public String getPayfail_details_id() {
        return payfail_details_id;
    }

    public void setPayfail_details_id(String payfail_details_id) {
        this.payfail_details_id = payfail_details_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp_send_on() {
        return otp_send_on;
    }

    public void setOtp_send_on(String otp_send_on) {
        this.otp_send_on = otp_send_on;
    }

    public String getWhatsapp_no() {
        return whatsapp_no;
    }

    public void setWhatsapp_no(String whatsapp_no) {
        this.whatsapp_no = whatsapp_no;
    }

    public String getNo_of_orders() {
        return no_of_orders;
    }

    public void setNo_of_orders(String no_of_orders) {
        this.no_of_orders = no_of_orders;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTime_left() {
        return time_left;
    }

    public void setTime_left(String time_left) {
        this.time_left = time_left;
    }

    public String getIs_cod_available() {
        return is_cod_available;
    }

    public void setIs_cod_available(String is_cod_available) {
        this.is_cod_available = is_cod_available;
    }
}
