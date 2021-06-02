package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookkr.user.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ModelVariantAdapter extends RecyclerView.Adapter<ModelVariantAdapter.ViewHolder> {

    Context context;
    private ArrayList<String> list;

    public ModelVariantAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ModelVariantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_model_varient_item, parent, false);
        return new ModelVariantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String bean = list.get(position);
        int i = position+1;
        holder.adapter_textview_modal.setText(i+". "+bean+"");
//        holder.tvVariant.setText(bean.getVariantName());
//        holder.tvPrice.setText(context.getResources().getString(R.string.rupee_sign)+" "+bean.getPrice());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView adapter_textview_modal;
//        TextView tvVariant;
//        TextView tvPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            adapter_textview_modal = itemView.findViewById(R.id.adapter_textview_modal);
//            tvVariant = itemView.findViewById(R.id.tvVariant);
//            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
