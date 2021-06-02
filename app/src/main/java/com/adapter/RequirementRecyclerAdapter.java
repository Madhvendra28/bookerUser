package com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookkr.user.R;
import com.bookkr.user.RequirementDetailsActivity;
import com.bookkr.user.RequirementListActivity;
import com.fragment.RequirementClaimDialogFragment;
import com.model.PostRequirement;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RequirementRecyclerAdapter extends RecyclerView.Adapter<RequirementRecyclerAdapter.ViewHolder> {
    private static final String TAG = "PostReqRecyclerAdapter";
    private List<PostRequirement> list;
    private Context context;

    public RequirementRecyclerAdapter(Context context, List<PostRequirement> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RequirementRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_req_item_layout, parent, false);
        return new RequirementRecyclerAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final PostRequirement postRequirement = list.get(position);

            holder.adapter_textview_product_name.setText(postRequirement.getModel_name());
            holder.adapter_textview_date.setText(AppUtils.convertDateWithoutTime(postRequirement.getStart_date()) + "");
            holder.adapter_textview_start_time.setText(AppUtils.getFormattedDateTime(postRequirement.getStart_timing()) + "");
            holder.adapter_textview_sale_type.setText(postRequirement.getSale_type() + "");
            holder.adapter_textview_claimed.setText(postRequirement.getClaim_quantity() + "");
            holder.adapter_textview_total_quantity.setText(postRequirement.getRequired_quantity() + "");
            holder.adapter_textview_dealer_name.setText(postRequirement.getDealer_name() + "");

            try {
                int claimedQty = Integer.parseInt(postRequirement.getClaim_quantity());
                int reqQty = Integer.parseInt(postRequirement.getRequired_quantity());

                if (reqQty > claimedQty) {
                    holder.adapter_textview_claim_available_tag.setVisibility(View.VISIBLE);
                    holder.adapter_button_claim_now.setVisibility(View.VISIBLE);
                } else {
                    holder.adapter_textview_claim_available_tag.setVisibility(View.GONE);
                    holder.adapter_button_claim_now.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RequirementDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppURLParams.position, position);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            holder.adapter_button_claim_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Spconstant.isForClaimorUpdate =true;
                    RequirementClaimDialogFragment claimDialogFragment = new RequirementClaimDialogFragment(
                            postRequirement.getRequirement_id(), postRequirement.getEvent_id(),
                            postRequirement.getRequired_quantity(), postRequirement.getClaim_quantity(), AppURLParams.statusVal1);
                    claimDialogFragment.show(((RequirementListActivity) context).getSupportFragmentManager(), AppURLParams.claimDialogFragment);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView adapter_imageview_req_Image;
        private TextView adapter_textview_claimed, adapter_textview_total_quantity, adapter_textview_claim_available_tag,
                adapter_textview_product_name, adapter_textview_date, adapter_textview_start_time, adapter_textview_sale_type,
                adapter_textview_dealer_name;
        private Button adapter_button_claim_now;

        public ViewHolder(View itemView) {
            super(itemView);
            adapter_imageview_req_Image = itemView.findViewById(R.id.imgReqImage);
//            adapter_imageview_action_star = itemView.findViewById(R.id.adapter_imageview_action_star);

            adapter_textview_claimed = itemView.findViewById(R.id.tvClaimed);
            adapter_textview_total_quantity = itemView.findViewById(R.id.tvTotalQuantity);
            adapter_textview_claim_available_tag = itemView.findViewById(R.id.adapter_textview_claim_available_tag);
            adapter_textview_product_name = itemView.findViewById(R.id.tvProductName);
            adapter_textview_date = itemView.findViewById(R.id.tvDate);
            adapter_textview_start_time = itemView.findViewById(R.id.tvStartTime);
            adapter_textview_sale_type = itemView.findViewById(R.id.tvSaleType);
            adapter_textview_dealer_name = itemView.findViewById(R.id.tvDealerName);
//            adapter_textview_varient = itemView.findViewById(R.id.adapter_textview_varient);
//            adapter_textview_shop_name = itemView.findViewById(R.id.adapter_textview_shop_name);
//            adapter_textview_rating = itemView.findViewById(R.id.adapter_textview_rating);
            adapter_button_claim_now = itemView.findViewById(R.id.btnClaimNow);

        }
    }
}
