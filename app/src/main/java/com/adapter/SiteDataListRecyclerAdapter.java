package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.bookkr.user.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.model.SiteData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SiteDataListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<SiteData> list;

    public SiteDataListRecyclerAdapter(Activity activity, ArrayList<SiteData> siteData) {
        this.activity = activity;
        this.list = siteData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_sites_address_generator, parent, false);
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

        public CheckBox adapter_checkBox_site_name;
        public LinearLayout llList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //   company_name = itemView.findViewById(R.id.company_name);
            adapter_checkBox_site_name = itemView.findViewById(R.id.adapter_checkBox_site_name);
            llList = itemView.findViewById(R.id.llList);
        }

        private void bindProductListItems(SiteDataListRecyclerAdapter adapter, final int position) {
            try {
                final SiteData siteData = list.get(position);
                adapter_checkBox_site_name.setText(siteData.getSite_name() + "");
                adapter_checkBox_site_name.setChecked(list.get(position).isChecked());

                adapter_checkBox_site_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            list.get(position).setChecked(true);
                        } else {
                            list.get(position).setChecked(false);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}