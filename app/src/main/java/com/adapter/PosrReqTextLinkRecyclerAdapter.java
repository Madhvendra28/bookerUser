package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.TextLink;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PosrReqTextLinkRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<TextLink> list;

    public PosrReqTextLinkRecyclerAdapter(Activity activity, ArrayList<TextLink> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_requirement_text_link_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder) holder).bindAddressListItems(this, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView adapter_textview_text, adapter_textview_link;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_textview_text = itemView.findViewById(R.id.adapter_textview_text);
            adapter_textview_link = itemView.findViewById(R.id.adapter_textview_link);
        }

        private void bindAddressListItems(PosrReqTextLinkRecyclerAdapter adapter, final int position) {
            try {
                final TextLink textLink = list.get(position);
                adapter_textview_text.setText(textLink.getText() + "");
                adapter_textview_link.setText(textLink.getPost_link() + "");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}