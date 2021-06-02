package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.Address;
import com.utils.AppURLParams;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequirementAddressesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Address> list;

    public RequirementAddressesRecyclerAdapter(Activity activity, ArrayList<Address> siteData) {
        this.activity = activity;
        this.list = siteData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_address_item_layout, parent, false);
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

        private LinearLayout adapter_LL_container, adapter_LL_operation;
        private TextView adapter_textview_website, adapter_textview_payment_mode, adapter_textview_name, adapter_textview_house_number,
                adapter_textview_shop_tags, adapter_textview_colony, adapter_textview_area, adapter_textview_landmark, adapter_textview_pincode,
                adapter_textview_city, adapter_textview_state, adapter_textview_note_for_site, adapter_textview_auto_fill_link,
                adapter_textview_address, adapter_textview_contact_no;
        private ImageView adapter_imageview_edit, adapter_imageview_delete, adapter_imageview_share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_website = itemView.findViewById(R.id.adapter_textview_website);
            adapter_textview_payment_mode = itemView.findViewById(R.id.adapter_textview_payment_mode);
            adapter_textview_name = itemView.findViewById(R.id.adapter_textview_name);
            adapter_textview_house_number = itemView.findViewById(R.id.adapter_textview_house_number);
            adapter_textview_shop_tags = itemView.findViewById(R.id.adapter_textview_shop_tags);
            adapter_textview_contact_no = itemView.findViewById(R.id.adapter_textview_contact_no);
            adapter_textview_address = itemView.findViewById(R.id.adapter_textview_address);
            adapter_textview_colony = itemView.findViewById(R.id.adapter_textview_colony);
            adapter_textview_area = itemView.findViewById(R.id.adapter_textview_area);
            adapter_textview_landmark = itemView.findViewById(R.id.adapter_textview_landmark);
            adapter_textview_pincode = itemView.findViewById(R.id.adapter_textview_pincode);
            adapter_textview_city = itemView.findViewById(R.id.adapter_textview_city);
            adapter_textview_state = itemView.findViewById(R.id.adapter_textview_state);
            adapter_textview_note_for_site = itemView.findViewById(R.id.adapter_textview_note_for_site);
            adapter_textview_auto_fill_link = itemView.findViewById(R.id.adapter_textview_auto_fill_link);

            adapter_LL_operation = itemView.findViewById(R.id.adapter_LL_operation);
            adapter_imageview_edit = itemView.findViewById(R.id.adapter_imageview_edit);
            adapter_imageview_delete = itemView.findViewById(R.id.adapter_imageview_delete);
            adapter_imageview_share = itemView.findViewById(R.id.adapter_imageview_share);
        }

        private void bindAddressListItems(RequirementAddressesRecyclerAdapter adapter, final int position) {
            try {
                final Address address = list.get(position);
                adapter_textview_website.setText(address.getSite_name() + "");
                adapter_textview_payment_mode.setText(address.getPayment_option() + "");
                adapter_textview_name.setText(address.getName() + " " + address.getSurname() + ", " + address.getName_code());
                adapter_textview_contact_no.setText(address.getContact_no() + "");
                adapter_textview_address.setText(address.getAddress() + "");
//                adapter_textview_colony.setText(address.getColony_name() + "");
//                adapter_textview_area.setText(address.getArea() + "");
//                adapter_textview_landmark.setText(address.getLandmark() + "");
//                adapter_textview_pincode.setText(address.getPostal_code() + "");
                adapter_textview_city.setText(address.getCity() + "");
                adapter_textview_state.setText(address.getState() + "");
                adapter_textview_note_for_site.setText(address.getNote().trim() + "");
                adapter_textview_auto_fill_link.setText(address.getLink() + "");

                adapter_LL_operation.setVisibility(View.GONE);

//                if (callingFrom.equalsIgnoreCase(AppURLParams.statusVal1)) {
//                    adapter_LL_operation.setVisibility(View.VISIBLE);
//                } else if (callingFrom.equalsIgnoreCase(AppURLParams.statusVal2)) {
//                    adapter_LL_operation.setVisibility(View.GONE);
//                }

                String s = address.getHouse_name().replace("||", " / ");
                adapter_textview_house_number.setText(s + "");

                s = address.getShop_tag();
                if (s.equalsIgnoreCase(AppURLParams.statusVal1)) {
                    adapter_textview_shop_tags.setText(activity.getString(R.string.yes));
                } else {
                    adapter_textview_shop_tags.setText(activity.getString(R.string.no));
                }

                /*try {
                    JSONArray jsonArray = new JSONArray(address.getSite_name());
                    s = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        s += jsonArray.get(i).toString().trim();

                        if (i != jsonArray.length() - 1) {
                            s += ", ";
                        }
                    }
                    adapter_textview_website.setText(s + "");
                } catch (Exception e) {
                }*/

//                adapter_imageview_edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        handItemClick(address);
//                    }
//                });

//                adapter_imageview_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

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
    }
}