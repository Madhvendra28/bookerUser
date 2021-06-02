package com.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookkr.user.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
    View itemView;
    public ImageView imageViewBackground;
    ImageView imageGifContainer;
    TextView textViewDescription;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
        this.itemView = itemView;
    }
}
