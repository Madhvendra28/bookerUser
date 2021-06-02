package com.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimUpdateShippingActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.ModalVariant;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class UpdateShippingModalVariantAdapter extends RecyclerView.Adapter<UpdateShippingModalVariantAdapter.ViewHolder> {

    private UserClaimUpdateShippingActivity activity;
    private ArrayList<String> stringArrayList;
    private ArrayList<ModalVariant> modalVariantArrayList;
    private ModalVariant selectedModalVariant;

    public UpdateShippingModalVariantAdapter(UserClaimUpdateShippingActivity activity, ArrayList<String> strings, ArrayList<ModalVariant> modalVariantArrayList) {
        this.activity = activity;
        this.stringArrayList = strings;
        this.modalVariantArrayList = modalVariantArrayList;
    }

    @Override
    public UpdateShippingModalVariantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shipping_modal_variant_item, parent, false);
        return new UpdateShippingModalVariantAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Spinner adapter_spinner_modal_color;
        private TextView adapter_textview_vaiant_name, adapter_textview_vaiant_price, adapter_edittext_advance_paid, adapter_textview_qty;
        private ImageView adapter_imageview_minus, adapter_imageview_plus;

        public ViewHolder(View itemView) {
            super(itemView);

            adapter_textview_vaiant_name = itemView.findViewById(R.id.adapter_textview_vaiant_name);
            adapter_textview_vaiant_price = itemView.findViewById(R.id.adapter_textview_vaiant_price);
            adapter_textview_qty = itemView.findViewById(R.id.adapter_textview_qty);

            adapter_spinner_modal_color = itemView.findViewById(R.id.adapter_spinner_modal_color);
            adapter_edittext_advance_paid = itemView.findViewById(R.id.adapter_edittext_advance_paid);

            adapter_imageview_minus = itemView.findViewById(R.id.adapter_imageview_minus);
            adapter_imageview_plus = itemView.findViewById(R.id.adapter_imageview_plus);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final ModalVariant modalVariant = modalVariantArrayList.get(position);
            selectedModalVariant = modalVariant;

            holder.adapter_textview_vaiant_name.setText(modalVariant.getVariant_name() + "");
            holder.adapter_textview_vaiant_price.setText(modalVariant.getVariant_price() + "");
            holder.adapter_edittext_advance_paid.setText(modalVariant.getAdvance_paid() + "");

            String s = modalVariant.getVariant_color_str();
            final String[] strArray = AppUtils.getStringArrayTokenize(s);
            if (strArray != null) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, strArray);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.adapter_spinner_modal_color.setAdapter(dataAdapter);

                holder.adapter_spinner_modal_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            modalVariant.setSelected_color(strArray[position]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            holder.adapter_edittext_advance_paid.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        modalVariant.setAdvance_paid(s + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            holder.adapter_imageview_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int count = Integer.parseInt(holder.adapter_textview_qty.getText().toString()) + 1;

                        int maxLimit = 0, alreadyDone = 0, totalCount = 0;
                        String paymentMode = activity.getPaymentMode();

                        if (paymentMode.equals(AppURLParams.statusVal1)) {
                            maxLimit = Integer.parseInt(modalVariant.getCod_quantity());
                            alreadyDone = Integer.parseInt(modalVariant.getShipped_cod_quantity());

                        } else if (paymentMode.equals(AppURLParams.statusVal2)) {
                            maxLimit = Integer.parseInt(modalVariant.getPrepaid_quantity());
                            alreadyDone = Integer.parseInt(modalVariant.getShipped_prepaid_quantity());

                        } else if (paymentMode.equals(AppURLParams.statusVal3)) {
                            maxLimit = Integer.parseInt(modalVariant.getPayfail_quantity());
                            alreadyDone = Integer.parseInt(modalVariant.getShipped_payfail_quantity());

                        } else if (paymentMode.equals(AppURLParams.statusVal4)) {
                            maxLimit = Integer.parseInt(modalVariant.getOtp_verify());
                            alreadyDone = Integer.parseInt(modalVariant.getShipped_otp_verify_quantity());
                        }

                        totalCount = count + alreadyDone;
                        if (totalCount > maxLimit) {
                            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), activity.getString(R.string.error_quantity_exceeds), Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        holder.adapter_textview_qty.setText(String.valueOf(count));
                        modalVariant.setShipping_quantity(count + "");
                    } catch (Exception e) {
                        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), activity.getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            holder.adapter_imageview_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int count = Integer.parseInt(holder.adapter_textview_qty.getText().toString());
                        if (count == 0) {
                            return;
                        }
                        count--;
                        holder.adapter_textview_qty.setText(String.valueOf(count));
                        modalVariant.setShipping_quantity(count + "");
                    } catch (Exception e) {
                        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), activity.getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
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
