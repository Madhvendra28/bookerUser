package com.bookkr.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WalletDetailsActivity extends AppCompatActivity {
    RecyclerView walletList;
    LinearLayoutManager layoutManager;
    LinearLayout llSelf, llOther;
    TextView tvSelf, tvOther;
    List<ModelWallet> models;
 //   WalletListAdapter adapter;
    public static final int PERMISSION_WRITE = 0;

    private static AppCompatActivity activity;
    private int requestFor = -1;
    private ProgressDialog progress;
    private final String TAG = "Requirement List";


    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);
        activity = this;

        walletList = findViewById(R.id.walletList);
        llSelf = findViewById(R.id.llSelf);
        llOther = findViewById(R.id.llOther);
        tvSelf = findViewById(R.id.tvSelf);
        tvOther = findViewById(R.id.tvOther);

    }

    class ModelWallet {
        String wallet_id, wallet_name_id, add_type, phone_no;
        String wallet_name, banking_name, upi_id, image, qr_code_full_image, share_data;

        public ModelWallet(String wallet_id, String wallet_name_id, String add_type, String phone_no, String wallet_name, String banking_name, String upi_id, String image, String qr_code_full_image, String share_data) {
            this.wallet_id = wallet_id;
            this.wallet_name_id = wallet_name_id;
            this.add_type = add_type;
            this.phone_no = phone_no;
            this.wallet_name = wallet_name;
            this.banking_name = banking_name;
            this.upi_id = upi_id;
            this.image = image;
            this.qr_code_full_image = qr_code_full_image;
            this.share_data = share_data;
        }

        public String getShare_data() {
            return share_data;
        }

        public void setShare_data(String share_data) {
            this.share_data = share_data;
        }

        public String getWallet_id() {
            return wallet_id;
        }

        public void setWallet_id(String wallet_id) {
            this.wallet_id = wallet_id;
        }

        public String getWallet_name_id() {
            return wallet_name_id;
        }

        public void setWallet_name_id(String wallet_name_id) {
            this.wallet_name_id = wallet_name_id;
        }

        public String getAdd_type() {
            return add_type;
        }

        public void setAdd_type(String add_type) {
            this.add_type = add_type;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getWallet_name() {
            return wallet_name;
        }

        public void setWallet_name(String wallet_name) {
            this.wallet_name = wallet_name;
        }

        public String getBanking_name() {
            return banking_name;
        }

        public void setBanking_name(String banking_name) {
            this.banking_name = banking_name;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getQr_code_full_image() {
            return qr_code_full_image;
        }

        public void setQr_code_full_image(String qr_code_full_image) {
            this.qr_code_full_image = qr_code_full_image;
        }
    }

}