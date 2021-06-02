package com.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bookkr.user.ProfileActivity;
import com.bookkr.user.R;
import com.listnerr.RemoveOneItemsListener;
import com.model.UpdateProfile;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpdateProfileAdapter extends RecyclerView.Adapter<UpdateProfileAdapter.UpdateProfileHolder> {

    private final List<UpdateProfile> list;
    private final Context context;
    private RemoveOneItemsListener removeOneItemsListener;

    public void setRemoveOneItemsListener(RemoveOneItemsListener removeOneItemsListener) {
        this.removeOneItemsListener = removeOneItemsListener;
    }

    public UpdateProfileAdapter(Context context, List<UpdateProfile> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public UpdateProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.adapter_update_profile_layout,parent,false);
        return new UpdateProfileHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final UpdateProfileHolder holder, final int position) {

        UpdateProfile updateProfile= list.get(position);
      //  holder.UpdateProfile.setText(updateProfile.getUpdate());
        /*if(position == 0) {
            holder.UpdateProfile.setText("Update Profile");
        }else if(position == 1){
            holder.UpdateProfile.setText("How to use");
        }else if(position == 2){
            holder.UpdateProfile.setText("Update Profile");
        }*/

        holder.UpdateProfile.setText(updateProfile.getUpdate());
        holder.updateProfile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        holder.img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                holder.itemView.startAnimation(animation);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        removeOneItemsListener.removeItem(position);
                    }
                }, 500);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class UpdateProfileHolder extends RecyclerView.ViewHolder {

        private final ImageView Profile;
        private  ImageView img_cross;
        private final TextView UpdateProfile;
        private RelativeLayout updateProfile_layout;

        public UpdateProfileHolder(@NonNull View itemView) {
            super(itemView);

            Profile=itemView.findViewById(R.id.dummy_image);
            img_cross=itemView.findViewById(R.id.img_cross);
            UpdateProfile=itemView.findViewById(R.id.dummy_text);
            updateProfile_layout=itemView.findViewById(R.id.updateProfile_layout);

        }
    }

}
