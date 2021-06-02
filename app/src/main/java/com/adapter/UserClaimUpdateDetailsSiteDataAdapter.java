package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimUpdateDetailsActivity;
import com.model.SiteData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimUpdateDetailsSiteDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserClaimUpdateDetailsActivity activity;
    private ArrayList<SiteData> list;

    public UserClaimUpdateDetailsSiteDataAdapter(UserClaimUpdateDetailsActivity activity, ArrayList<SiteData> siteData) {
        this.activity = activity;
        this.list = siteData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_sites_name_item_radio, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder) holder).bindProductListItems(this, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RadioButton adapter_radiobutton_site_name;
        public LinearLayout llList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_radiobutton_site_name = itemView.findViewById(R.id.adapter_radiobutton_site_name);
            llList = itemView.findViewById(R.id.llList);
        }

        private void bindProductListItems(UserClaimUpdateDetailsSiteDataAdapter adapter, final int position) {
            try {
                final SiteData siteData = list.get(position);
                adapter_radiobutton_site_name.setText(siteData.getSite_name() + "");
                adapter_radiobutton_site_name.setChecked(siteData.isChecked());
                if (siteData.isChecked()) {
                    activity.setFragmentForSite(siteData);
                }

                adapter_radiobutton_site_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            for (int i = 0; i < list.size(); i++) {
                                SiteData siteData1 = list.get(i);
                                siteData1.setChecked(false);
                            }
                            siteData.setChecked(true);
                            activity.setFragmentForSite(siteData);
                            notifyDataSetChanged();

                        } else {
                            siteData.setChecked(false);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}