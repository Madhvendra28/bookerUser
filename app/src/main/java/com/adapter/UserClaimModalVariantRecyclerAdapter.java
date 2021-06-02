package com.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimPayFailDetailsActivity;
import com.model.ModalVariant;
import com.model.SiteData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimModalVariantRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private SiteData siteData;
    private ArrayList<ModalVariant> variantDataArrayList;

    public UserClaimModalVariantRecyclerAdapter(Activity activity, SiteData siteData, ArrayList<ModalVariant> variantDataArrayList) {
        this.activity = activity;
        this.siteData = siteData;
        this.variantDataArrayList = variantDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_user_claim_variant_recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return variantDataArrayList.size();
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

        private TextView adapter_textview_variant_name, adapter_textview_pay_fail_update;
        private EditText adapter_edittext_cod, adapter_edittext_pre_paid, adapter_edittext_pay_fail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_variant_name = itemView.findViewById(R.id.adapter_textview_variant_name);
            adapter_edittext_cod = itemView.findViewById(R.id.adapter_edittext_cod);
            adapter_edittext_pre_paid = itemView.findViewById(R.id.adapter_edittext_pre_paid);
            adapter_edittext_pay_fail = itemView.findViewById(R.id.adapter_edittext_pay_fail);
            adapter_textview_pay_fail_update = itemView.findViewById(R.id.adapter_textview_pay_fail_update);
        }

        private void bindAddressListItems(UserClaimModalVariantRecyclerAdapter adapter, final int position) {
            try {
                final ModalVariant modalVariant = variantDataArrayList.get(position);

                adapter_textview_variant_name.setText(modalVariant.getVariant_name() + "");
                adapter_edittext_cod.setText(modalVariant.getCod_quantity() + "");
                adapter_edittext_pre_paid.setText(modalVariant.getPrepaid_quantity() + "");
                adapter_edittext_pay_fail.setText(modalVariant.getPayfail_quantity() + "");
//                adapter_textview_otp_verified.setText(modalVariant.getOtp_verify() + "");

                adapter_edittext_cod.addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void afterTextChanged(Editable s) {
                        String string = s.toString();
                        modalVariant.setCod_quantity(string);
                    }
                });

                adapter_edittext_pre_paid.addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void afterTextChanged(Editable s) {
                        String string = s.toString();
                        modalVariant.setPrepaid_quantity(string);
                    }
                });

                adapter_edittext_pay_fail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            adapter_edittext_pay_fail.setError(null);
                            Intent intent = new Intent(activity, UserClaimPayFailDetailsActivity.class);
                            activity.startActivityForResult(intent, 80);
                            activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                adapter_edittext_pay_fail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        try {
                            if (hasFocus) {
                                adapter_edittext_pay_fail.setError(null);
                                Intent intent = new Intent(activity, UserClaimPayFailDetailsActivity.class);
                                activity.startActivityForResult(intent, 80);
                                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                adapter_textview_pay_fail_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            adapter_edittext_pay_fail.setError(null);
                            Intent intent = new Intent(activity, UserClaimPayFailDetailsActivity.class);
                            activity.startActivityForResult(intent, 80);
                            activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}