package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimUpdateDetailsActivity;
import com.model.ModalVariant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimUpdateModalVariantRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserClaimUpdateDetailsActivity activity;
    private ArrayList<ModalVariant> modalVariantArrayList;

    public UserClaimUpdateModalVariantRecyclerAdapter(UserClaimUpdateDetailsActivity activity, ArrayList<ModalVariant> modalVariantArrayList) {
        this.activity = activity;
        this.modalVariantArrayList = modalVariantArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_user_claim_update_variant_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return modalVariantArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder) holder).bindAddressListItems(this, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView adapter_textview_variant_name, adapter_textview_cod, adapter_textview_pre_paid, adapter_textview_pay_fail,
                adapter_textview_otp_verify;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_variant_name = itemView.findViewById(R.id.adapter_textview_variant_name);
            adapter_textview_cod = itemView.findViewById(R.id.adapter_textview_cod);
            adapter_textview_pre_paid = itemView.findViewById(R.id.adapter_textview_pre_paid);
            adapter_textview_pay_fail = itemView.findViewById(R.id.adapter_textview_pay_fail);
            adapter_textview_otp_verify = itemView.findViewById(R.id.adapter_textview_otp_verify);

        }

        private void bindAddressListItems(UserClaimUpdateModalVariantRecyclerAdapter adapter, final int position) {
            try {
                final ModalVariant modalVariant = modalVariantArrayList.get(position);
                adapter_textview_variant_name.setText(modalVariant.getVariant_name() + "");
                adapter_textview_cod.setText(modalVariant.getCod_quantity() + "/" + modalVariant.getShipped_cod_quantity());
                adapter_textview_pre_paid.setText(modalVariant.getPrepaid_quantity() + "/" + modalVariant.getShipped_prepaid_quantity());
                adapter_textview_pay_fail.setText(modalVariant.getPayfail_quantity() + "/" + modalVariant.getShipped_payfail_quantity());
                adapter_textview_otp_verify.setText(modalVariant.getOtp_quantity() + "/" + modalVariant.getShipped_otp_verify_quantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}