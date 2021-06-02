package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimConfirmActivity;
import com.bookkr.user.UserClaimHistoryListActivity;
import com.bookkr.user.UserClaimUpdateDetailsActivity;
import com.model.UserClaim;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import java.util.ArrayList;

public class UserClaimHistoryListAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<UserClaim> list;

    public UserClaimHistoryListAdapter(Activity activity, ArrayList<UserClaim> list) {
        super(activity, R.layout.adapter_user_claim_history_item_2, list);
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
        View rowView = inflater.inflate(R.layout.adapter_user_claim_history_item_2, parent, false);

        try {
            final UserClaim userClaim = list.get(position);
            TextView adapter_textview_claimed = rowView.findViewById(R.id.tvClaimed);
            TextView adapter_textview_total_quantity = rowView.findViewById(R.id.tvTotalQuantity);
            TextView adapter_textview_claim_available_tag = rowView.findViewById(R.id.adapter_textview_claim_available_tag);
            TextView adapter_textview_product_name = rowView.findViewById(R.id.tvProductName);
            TextView adapter_textview_date = rowView.findViewById(R.id.tvDate);
            TextView adapter_textview_start_time = rowView.findViewById(R.id.tvStartTime);
            TextView adapter_textview_sale_type = rowView.findViewById(R.id.tvSaleType);
            TextView adapter_textview_dealer_name = rowView.findViewById(R.id.tvDealerName);

//            ImageView adapter_imageview_req_Image = rowView.findViewById(R.id.adapter_imageview_req_Image);
            Button adapter_button_claim_update = rowView.findViewById(R.id.adapter_button_claim_update);
            Button adapter_button_confirm_claim = rowView.findViewById(R.id.adapter_button_confirm_claim);

            adapter_textview_product_name.setText(userClaim.getModel_name());
            adapter_textview_date.setText(AppUtils.convertDateWithoutTime(userClaim.getStart_date()) + "");
            adapter_textview_start_time.setText(AppUtils.getFormattedDateTime(userClaim.getStart_timing()) + "");
            adapter_textview_sale_type.setText(userClaim.getSale_type() + "");
            adapter_textview_dealer_name.setText(userClaim.getDealer_name() + "");

            adapter_textview_claimed.setText(userClaim.getConfirmed_quantity() + "");
            adapter_textview_total_quantity.setText(userClaim.getQuantity() + "");

            try {
                int claimedQty = Integer.parseInt(userClaim.getQuantity());
                int reqQty = Integer.parseInt(userClaim.getRequired_quantity());

                if (reqQty > claimedQty) {
                    adapter_textview_claim_available_tag.setVisibility(View.VISIBLE);
//                    adapter_button_confirm_claim.setVisibility(View.VISIBLE);
                } else {
                    adapter_textview_claim_available_tag.setVisibility(View.GONE);
//                    adapter_button_confirm_claim.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }

            adapter_button_claim_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((UserClaimHistoryListActivity) activity).setSelectedUserClaim(userClaim);

                        Intent intent = new Intent(activity, UserClaimUpdateDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AppURLParams.position, position);
                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent, 60);
                        activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            adapter_button_confirm_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((UserClaimHistoryListActivity) activity).setSelectedUserClaim(userClaim);

                        Intent intent = new Intent(activity, UserClaimConfirmActivity.class);
                        activity.startActivityForResult(intent, 70);
                        activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
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