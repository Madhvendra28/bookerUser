package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.ModalVariant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostReqModalVariantRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<ModalVariant> list;

    public PostReqModalVariantRecyclerAdapter(Activity activity, ArrayList<ModalVariant> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_requirement_variant_item_layout, parent, false);
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

        private TextView adapter_textview_variant_name, adapter_textview_modal_color, adapter_textview_rate_type, adapter_textview_cod,
                adapter_textview_pre_paid, adapter_textview_pay_fail, adapter_textview_otp_verified;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_variant_name = itemView.findViewById(R.id.adapter_textview_variant_name);
            adapter_textview_modal_color = itemView.findViewById(R.id.adapter_textview_modal_color);
            adapter_textview_rate_type = itemView.findViewById(R.id.adapter_textview_rate_type);
            adapter_textview_cod = itemView.findViewById(R.id.adapter_textview_cod);
            adapter_textview_pre_paid = itemView.findViewById(R.id.adapter_textview_pre_paid);
            adapter_textview_pay_fail = itemView.findViewById(R.id.adapter_textview_pay_fail);
            adapter_textview_otp_verified = itemView.findViewById(R.id.adapter_textview_otp_verified);
        }

        private void bindAddressListItems(PostReqModalVariantRecyclerAdapter adapter, final int position) {
            try {
                final ModalVariant modalVariant = list.get(position);
                adapter_textview_variant_name.setText(modalVariant.getVariant_name() + "");
                adapter_textview_modal_color.setText(modalVariant.getVariant_color_str() + "");
                adapter_textview_rate_type.setText(modalVariant.getRate_type() + "");
                adapter_textview_cod.setText(modalVariant.getCod_price() + "");
                adapter_textview_pre_paid.setText(modalVariant.getPrepaid_price() + "");
                adapter_textview_pay_fail.setText(modalVariant.getPayfail_price() + "");
                adapter_textview_otp_verified.setText(modalVariant.getOtp_verify() + "");

                /*adapter_imageview_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String houseName = address.getHouse_name().replace("||", " / ");
                            String shopName = address.getShop_name();

                            Intent sendIntent1 = new Intent();
                            sendIntent1.setAction(Intent.ACTION_SEND);
                            sendIntent1.putExtra(Intent.EXTRA_TEXT,
                                    "Name: " + address.getName() + "\n" +
                                            "House No.: " + houseName + "\n" +
                                            "Store Name: " + shopName + "\n" +
                                            "LandMark: " + address.getLandmark() + "\n" +
                                            "Colony: " + address.getColony_name() + "\n" +
                                            "Area: " + address.getArea() + "\n" +
                                            "Pincode: " + address.getPostal_code() + "\n" +
                                            "Payment Method: " + address.getPayment_option() + "\n" +
                                            "Name Code: " + address.getName_code() + "\n"
                            );
                            sendIntent1.setType("text/plain");
                            activity.startActivity(sendIntent1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*private void handItemClick(Address address) {
            try {
                Intent intent = new Intent(activity, AddressGeneratorEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppURLParams.address_id, address.getAddress_id() + "");
                bundle.putParcelable(AppURLParams.addressDetailsObj, address);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, 330);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}