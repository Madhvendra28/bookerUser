package com;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkr.user.R;
import com.google.android.material.button.MaterialButton;
import com.model.SiteData;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePreviewDialog extends Dialog {
    private Activity activity;
    String imageUrl;

    public ImagePreviewDialog(Activity activity, String imageUrl) {
        super(activity);
        this.activity = activity;
        this.imageUrl = imageUrl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_preview);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TouchImageView eventImage1 = findViewById(R.id.eventImage1);
        ImageView imgClose = findViewById(R.id.imgClose);
        Picasso.get().load(imageUrl.replace("https", "http")).into(eventImage1);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewDialog.this.dismiss();
            }
        });
    }
}
