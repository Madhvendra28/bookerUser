package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookkr.user.R;
import com.bumptech.glide.Glide;
import com.model.OfferBanner;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.utils.AppURL;

import java.util.ArrayList;

public class OfferBannerImageSliderAdapter extends SliderViewAdapter<SliderAdapterVH> {

    private Context context;
    private ArrayList<OfferBanner> mSliderItems = new ArrayList<>();

    public OfferBannerImageSliderAdapter(Context context, ArrayList<OfferBanner> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_offer_banner_image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        try {
            final OfferBanner offerBanner = mSliderItems.get(position);
//            Log.d("offerBanner Image", offerBanner.getImage_path()+"");

            Glide.with(viewHolder.itemView)
                    .load(offerBanner.getImage_path())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

            /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(offerBanner.getUrl() + ""));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }
}
