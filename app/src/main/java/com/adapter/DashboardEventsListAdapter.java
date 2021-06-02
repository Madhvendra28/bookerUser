package com.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookkr.user.EventDetailsActivity;
import com.bookkr.user.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.model.Event;
import com.model.Site;
import com.squareup.picasso.Picasso;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardEventsListAdapter extends RecyclerView.Adapter<DashboardEventsListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Event> eventArrayList;

    public DashboardEventsListAdapter(Activity activity, ArrayList<Event> eventArrayList) {
        this.activity = activity;
        this.eventArrayList = eventArrayList;
    }

    @Override
    public DashboardEventsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
/*
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_event_item_layout, parent, false);
*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_adapter_event_item_layout, parent, false);
        return new DashboardEventsListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView adapter_imageview_event_image;
        private TextView adapter_textview_event_name, adapter_textview_site_name, adapter_textview_start_date, adapter_textview_end_date,
                adapter_textview_sale_type, adapter_textview_offer_title, adapter_textview_modal_name;
        private LinearLayout adapter_LL_end_date, adapter_LL_offer_title;
        private RecyclerView adapter_recyclerview_modal_variant;
        private Button adapter_button_post_requirement;
        RecyclerView recyclerImage;
        ImageView logoImg;

        public ViewHolder(View itemView) {
            super(itemView);
            // adapter_imageview_event_image = itemView.findViewById(R.id.adapter_imageview_event_image);
            adapter_textview_event_name = itemView.findViewById(R.id.adapter_textview_event_name);
            // adapter_textview_site_name = itemView.findViewById(R.id.adapter_textview_site_name);
            adapter_textview_start_date = itemView.findViewById(R.id.rate_chart_textview_start_date);
            //adapter_LL_end_date = itemView.findViewById(R.id.adapter_LL_end_date);
            adapter_textview_end_date = itemView.findViewById(R.id.rate_chart_textview_end_date);
            adapter_textview_sale_type = itemView.findViewById(R.id.tvSaleType);
            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            //adapter_LL_offer_title = itemView.findViewById(R.id.adapter_LL_offer_title);
            // adapter_textview_offer_title = itemView.findViewById(R.id.adapter_textview_offer_title);
            adapter_textview_modal_name = itemView.findViewById(R.id.rate_chart_textview_modal_name);
            logoImg = itemView.findViewById(R.id.logoImg);
            //  adapter_recyclerview_modal_variant = itemView.findViewById(R.id.adapter_recyclerview_modal_variant);
            // adapter_button_post_requirement = itemView.findViewById(R.id.adapter_button_post_requirement);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final Event event = eventArrayList.get(position);
            //holder.adapter_textview_event_name.setText(event.getEvent_name() + "");
            //    holder.adapter_textview_start_date.setText(AppUtils.parseDateToddMMyyyy(event.getStart_date()));
            Log.d("Error", event.getEvent_id());
            //   holder.adapter_textview_start_date.setText(""+(event.getStart_date()));
            // holder.adapter_textview_end_date.setText(""+(event.getEnd_date()));
            holder.adapter_textview_sale_type.setText("" + (event.getSale_type()));
            //  holder.adapter_textview_sale_type.setText(event.getSale_type() + "");
            holder.recyclerImage.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
            ArrayList<Site> sites = event.getSites();
            holder.recyclerImage.setAdapter(new SiteImageAdaptor(sites, event));

            if (event.getSale_type().equalsIgnoreCase("Open Sale")) {
                holder.logoImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.img_mobile_open_sale));
                holder.adapter_textview_modal_name.setText("" + (event.getEvent_name()));
                holder.adapter_textview_end_date.setText(AppUtils.parseDateForOpenSale(event.getStart_date()) + "-" + AppUtils.parseDateForOpenSale(event.getEnd_date()));
            } else {
                holder.adapter_textview_modal_name.setText("" + (event.getEventModelVarients().get(0).getModel_name()));
                holder.adapter_textview_end_date.setText(AppUtils.parseDateTodMMMEEEhmma(event.getEnd_date()));
            }

            holder.recyclerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EventDetailsActivity.class);
                    intent.putExtra(AppURLParams.event_id, event.getEvent_id());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ((HomeActivity) activity).setSelectedEvent(event);
                    Intent intent = new Intent(activity, EventDetailsActivity.class);
                    intent.putExtra(AppURLParams.event_id, event.getEvent_id());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });


          /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, RequirementAddActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra(AppURLParams.eventDetailsObj, gson.toJson(event));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                }
            });
          */ /* for(int i =0;i< sites.size();i++){
                try {
                    View child = LayoutInflater.from(activity).inflate(R.layout.layout_site_image, holder.layoutImages);
                    ImageView imageView = child.findViewById(R.id.imgSiteImage);
                    Glide.with(holder.layoutImages)
                            .load(sites.get(i).getSite_image())
                            .fitCenter()
                            .into(imageView);
                    holder.layoutImages.addView(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

           /* String s = "";
            for (int i = 0; i < event.getSite_name().size(); i++) {
                s += event.getSite_name().get(i);

                if (i != event.getSite_name().size() - 1) {
                    s += ", ";
                }
            }
            holder.adapter_textview_site_name.setText(s);
*/
        /*    if (event.getIs_offer().equals(AppURLParams.statusVal1)) {
                holder.adapter_LL_offer_title.setVisibility(View.VISIBLE);
                holder.adapter_textview_offer_title.setText(event.getOffer_title());
            } else {
                holder.adapter_LL_offer_title.setVisibility(View.GONE);
            }*/

            //  holder.adapter_textview_modal_name.setText(event.getModel_name() + "");
          /*  if (event.getModel_variant() != null) {
                ModelVariantAdapter adapter = new ModelVariantAdapter(activity, event.getModel_variant());
                holder.adapter_recyclerview_modal_variant.setLayoutManager(new GridLayoutManager(activity, 2));
                holder.adapter_recyclerview_modal_variant.setAdapter(adapter);
            }

            holder.adapter_button_post_requirement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, RequirementAddActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra(AppURLParams.eventDetailsObj, gson.toJson(event));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SiteImageAdaptor extends RecyclerView.Adapter<SiteImageAdaptor.ViewHolder> {
        ArrayList<Site> sites;
        Event event;

        public SiteImageAdaptor(ArrayList<Site> sites, Event event) {
            this.sites = sites;
            this.event = event;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_site_image, parent, false);
            return new SiteImageAdaptor.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           /* Glide.with(holder.itemView)
                    .load(sites.get(position).getSite_image())
                    .fitCenter()
                    .into(holder.imgSiteImage);*/
            Picasso.get().load(sites.get(position).getSite_image().replace("https", "http")).into(holder.imgSiteImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EventDetailsActivity.class);
                    intent.putExtra(AppURLParams.event_id, event.getEvent_id());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });

        }

        @Override
        public int getItemCount() {
            return sites.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSiteImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imgSiteImage = itemView.findViewById(R.id.imgSiteImage);
            }
        }
    }
}
