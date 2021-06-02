package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.Address;
import com.model.Event;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> stringArrayList;
    private ArrayList<Address> addressArrayList;
    private Address selectedAddress;

    public AddressRecyclerViewAdapter(Activity activity, ArrayList<String> strings,
                                      ArrayList<Address> addressArrayList) {
        this.activity = activity;
        this.stringArrayList = strings;
        this.addressArrayList = addressArrayList;
    }

    @Override
    public AddressRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_address_item_layout, parent, false);
        return new AddressRecyclerViewAdapter.ViewHolder(view);
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
            selectedAddress = addressArrayList.get(position);

//            holder.adapter_textview_vaiant_name.setText(selectedAddress.getVariant_name() + "");
//            holder.adapter_edittext_cod.setText(selectedAddress.getCod_price() + "");
//            holder.adapter_edittext_pre_paid.setText(selectedAddress.getPrepaid_price() + "");
//            holder.adapter_edittext_pay_fail.setText(selectedAddress.getPayfail_price() + "");
//            holder.adapter_edittext_otp_verified.setText(selectedAddress.getOtp_verify() + "");

//            VariantColorMulltiSelectSpinnerAdapter adapter = new VariantColorMulltiSelectSpinnerAdapter(activity, this, event.getVariant_color(), selectedAddress.getSelectedVariantColor());
//            holder.adapter_spinner_modal_color.setAdapter(adapter);

            final String[] arr = activity.getResources().getStringArray(R.array.rate_type_arrays);
//            holder.adapter_spinner_rate_type.setSelection(arr.);
            /*holder.adapter_spinner_rate_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        selectedAddress.setRate_type(arr[position]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
