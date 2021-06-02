package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.Event;
import com.model.ModalVariant;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ModalVariantRecyclerViewAdapter extends RecyclerView.Adapter<ModalVariantRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> stringArrayList;
    private ArrayList<ModalVariant> modalVariantArrayList;
    private Event event;
    private ModalVariant selectedModalVariant;

    public ModalVariantRecyclerViewAdapter(Activity activity, ArrayList<String> strings,
                                           ArrayList<ModalVariant> modalVariantArrayList, Event event) {
        this.activity = activity;
        this.stringArrayList = strings;
        this.modalVariantArrayList = modalVariantArrayList;
        this.event = event;
    }

    @Override
    public ModalVariantRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recyclerview_address_layout, parent, false);
        return new ModalVariantRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Spinner adapter_spinner_rate_type, adapter_spinner_modal_color;
        private TextView adapter_textview_vaiant_name, adapter_edittext_cod, adapter_edittext_pre_paid, adapter_edittext_pay_fail,
                adapter_edittext_otp_verified;

        public ViewHolder(View itemView) {
            super(itemView);

            adapter_textview_vaiant_name = itemView.findViewById(R.id.adapter_textview_vaiant_name);
            adapter_spinner_rate_type = itemView.findViewById(R.id.adapter_spinner_rate_type);
            adapter_spinner_modal_color = itemView.findViewById(R.id.adapter_spinner_modal_color);
            adapter_edittext_cod = itemView.findViewById(R.id.adapter_edittext_cod);
            adapter_edittext_pre_paid = itemView.findViewById(R.id.adapter_edittext_pre_paid);
            adapter_edittext_pay_fail = itemView.findViewById(R.id.adapter_edittext_pay_fail);
            adapter_edittext_otp_verified = itemView.findViewById(R.id.adapter_edittext_otp_verified);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
//            final String s = stringArrayList.get(position);
            final ModalVariant modalVariant = modalVariantArrayList.get(position);
            selectedModalVariant = modalVariant;

            holder.adapter_textview_vaiant_name.setText(modalVariant.getVariant_name() + "");
            holder.adapter_edittext_cod.setText(modalVariant.getCod_price() + "");
            holder.adapter_edittext_pre_paid.setText(modalVariant.getPrepaid_price() + "");
            holder.adapter_edittext_pay_fail.setText(modalVariant.getPayfail_price() + "");
            holder.adapter_edittext_otp_verified.setText(modalVariant.getOtp_verify() + "");

            VariantColorMulltiSelectSpinnerAdapter adapter = new VariantColorMulltiSelectSpinnerAdapter(activity, this, event.getVariant_color(), selectedModalVariant.getSelectedVariantColor());
            holder.adapter_spinner_modal_color.setAdapter(adapter);

            final String[] arr = activity.getResources().getStringArray(R.array.rate_type_arrays);
//            holder.adapter_spinner_rate_type.setSelection(arr.);
            holder.adapter_spinner_rate_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        modalVariant.setRate_type(arr[position]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.adapter_edittext_cod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String s = holder.adapter_edittext_cod.getText() + "";
                        modalVariant.setCod_price(s);
                    }
                }
            });

            holder.adapter_edittext_pre_paid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String s = holder.adapter_edittext_pre_paid.getText() + "";
                        modalVariant.setPrepaid_price(s);
                    }
                }
            });

            holder.adapter_edittext_pay_fail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String s = holder.adapter_edittext_pay_fail.getText() + "";
                        modalVariant.setPayfail_price(s);
                    }
                }
            });

            holder.adapter_edittext_otp_verified.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String s = holder.adapter_edittext_otp_verified.getText() + "";
                        modalVariant.setOtp_verify(s);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedColorModalVariantItems(ArrayList<String> list) {
        selectedModalVariant.setSelectedVariantColor(list);
    }

}
