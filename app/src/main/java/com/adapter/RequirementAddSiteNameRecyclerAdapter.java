package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkr.user.R;
import com.bumptech.glide.Glide;
import com.model.Site;
import java.util.ArrayList;

public class RequirementAddSiteNameRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Site> list;

    public RequirementAddSiteNameRecyclerAdapter(Activity activity, ArrayList<Site> siteData) {
        this.activity = activity;
        this.list = siteData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_requirement_add_site_name_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder) holder).bindSiteListItems(this, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView requirement_add_textview_site_name;
        private ImageView requirement_add_imageview_site_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            requirement_add_textview_site_name = itemView.findViewById(R.id.requirement_add_textview_site_name);
            requirement_add_imageview_site_image = itemView.findViewById(R.id.requirement_add_imageview_site_image);
        }

        private void bindSiteListItems(RequirementAddSiteNameRecyclerAdapter adapter, final int position) {
            try {
                final Site site = list.get(position);
                requirement_add_textview_site_name.setText(site.getSite_name() + "");
                Glide.with(itemView)
                        .load(site.getSite_image())
                        .fitCenter()
                        .into(requirement_add_imageview_site_image);
              //  Picasso.get().load(site.getSite_image()).error(R.mipmap.ic_launcher).into(requirement_add_imageview_site_image);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}