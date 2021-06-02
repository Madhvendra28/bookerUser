package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookkr.user.OrderHistoryActivity;
import com.bookkr.user.R;
import com.fragment.UpdateShipOrderDialogFragment;
import com.model.ShipOrderDetails;
import com.utils.AppURLParams;

import java.util.ArrayList;

public class ShipOrderHistoryListAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<ShipOrderDetails> list;

    public ShipOrderHistoryListAdapter(Activity activity, ArrayList<ShipOrderDetails> list) {
        super(activity, R.layout.adapter_ship_order_history_item, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_ship_order_history_item, parent, false);

        try {
            final ShipOrderDetails shipOrderDetails = list.get(position);
            TextView adapter_textview_site_name = rowView.findViewById(R.id.adapter_textview_site_name);
            TextView adapter_textview_dealer_name = rowView.findViewById(R.id.tvDealerName);
            TextView adapter_textview_requirement_id = rowView.findViewById(R.id.adapter_textview_requirement_id);
            TextView adapter_textview_name_on_order = rowView.findViewById(R.id.adapter_textview_name_on_order);
            TextView adapter_textview_modal_name = rowView.findViewById(R.id.adapter_textview_modal_name);
//            TextView adapter_textview_variant_name = rowView.findViewById(R.id.adapter_textview_variant_name);
            TextView adapter_textview_courier = rowView.findViewById(R.id.adapter_textview_courier);
            TextView adapter_textview_tracking_id = rowView.findViewById(R.id.adapter_textview_tracking_id);
            TextView adapter_textview_order_value = rowView.findViewById(R.id.adapter_textview_order_value);
            TextView adapter_textview_status = rowView.findViewById(R.id.adapter_textview_status);
            ImageView adapter_imageview_share = rowView.findViewById(R.id.adapter_imageview_share);
            Button adapter_button_order_update = rowView.findViewById(R.id.adapter_button_order_update);

            adapter_textview_site_name.setText(shipOrderDetails.getSite_name());
            adapter_textview_dealer_name.setText(shipOrderDetails.getDealer_name() + "");
            adapter_textview_requirement_id.setText("#" + shipOrderDetails.getRequirement_id());
            adapter_textview_name_on_order.setText(shipOrderDetails.getName_on_order() + "");
            adapter_textview_modal_name.setText(shipOrderDetails.getModel() + "");
            adapter_textview_courier.setText(shipOrderDetails.getCourier() + "");
            adapter_textview_tracking_id.setText(shipOrderDetails.getTracking_id() + "");
            adapter_textview_order_value.setText(activity.getString(R.string.rupees) + " " + shipOrderDetails.getOrder_value() + "");

//            ArrayList<ModalVariant> modalVariantArrayList = shipOrderDetails.getModalVariantArrayList();
//            String variantStr = "";
//            for (ModalVariant modalVariant : modalVariantArrayList) {
//                variantStr += modalVariant.getVariant_name() + " ";
//            }
//            adapter_textview_variant_name.setText(variantStr);

            if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal0)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_gray));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorGrey));
                adapter_textview_status.setText(activity.getString(R.string.status_shipped) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal1)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                adapter_textview_status.setText(activity.getString(R.string.status_reached_dc) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal2)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                adapter_textview_status.setText(activity.getString(R.string.status_ofd) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal3)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                adapter_textview_status.setText(activity.getString(R.string.status_undelivered) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal4)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                adapter_textview_status.setText(activity.getString(R.string.status_rejected) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal5)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                adapter_textview_status.setText(activity.getString(R.string.status_rto) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal6)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_green));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorGreen));
                adapter_textview_status.setText(activity.getString(R.string.status_delivered) + "");

            } else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal7)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_red));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorRed));
                adapter_textview_status.setText(activity.getString(R.string.status_donot_accept) + "");

            } /*else if (shipOrderDetails.getStatus().equalsIgnoreCase(AppURLParams.statusVal8)) {
                adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_red));
                adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorRed));
                adapter_textview_status.setText(activity.getString(R.string.status_missing_info) + "");

            }*/ else {
                adapter_textview_status.setText(activity.getString(R.string.na) + "");
            }

            adapter_button_order_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((OrderHistoryActivity) activity).setSelectedShipOrderDetails(shipOrderDetails);

                        UpdateShipOrderDialogFragment claimDialogFragment = new UpdateShipOrderDialogFragment(AppURLParams.statusVal1);
                        claimDialogFragment.show(((OrderHistoryActivity) activity).getSupportFragmentManager(), AppURLParams.shipOrderDialogFragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
            return rowView;
        }
    }
}