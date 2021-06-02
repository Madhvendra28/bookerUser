package com.adapter;

import android.app.Activity;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PayFailModalVariantRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<ModalVariant> list;

    public PayFailModalVariantRecyclerAdapter(Activity activity, ArrayList<ModalVariant> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_pay_fail_variant_recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
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

        private TextView adapter_textview_variant_name, adapter_textview_variant_price;
        private EditText adapter_edittext_fail_quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_variant_name = itemView.findViewById(R.id.adapter_textview_variant_name);
            adapter_textview_variant_price = itemView.findViewById(R.id.adapter_textview_variant_price);
            adapter_edittext_fail_quantity = itemView.findViewById(R.id.adapter_edittext_fail_quantity);
        }

        private void bindAddressListItems(PayFailModalVariantRecyclerAdapter adapter, final int position) {
            try {
                final ModalVariant modalVariant = list.get(position);
                adapter_textview_variant_name.setText(modalVariant.getVariant_name() + "");
                adapter_textview_variant_price.setText(modalVariant.getVariant_price() + "");
                adapter_edittext_fail_quantity.setText(modalVariant.getPayfail_quantity() + "");

                adapter_edittext_fail_quantity.addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void afterTextChanged(Editable s) {
                        String string = s.toString();
                        modalVariant.setPayfail_quantity(string);
                        ((UserClaimPayFailDetailsActivity) activity).updateAmountToPaid();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}