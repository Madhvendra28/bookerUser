package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkr.user.R;
import com.bookkr.user.UserClaimPayFailDetailsActivity;
import com.model.payfailModel.ModelDatum;

import java.util.List;

public class MdPayfailDataViewAdapter extends RecyclerView.Adapter<MdPayfailDataViewAdapter.PayfailDataViewHolder>{

    List<ModelDatum> modelData;
    UserClaimPayFailDetailsActivity activity;

    public MdPayfailDataViewAdapter(List<ModelDatum> modelData, UserClaimPayFailDetailsActivity activity) {
        this.modelData = modelData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PayfailDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.md_model_pay_fail_list_items, parent, false);
        return new PayfailDataViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PayfailDataViewHolder holder, int position) {

        Log.d("mdpayfailrv","model name "+modelData.get(position).getModelName());

        holder.modelName.setText(modelData.get(position).getModelName());


        holder.payFailModalVariantRecyclerAdapter = new PayFailModalVariantRecyclerAdapter(activity,modelData.get(position).getVariantData(),position);
        holder.variantRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        holder.variantRecyclerView.setAdapter(holder.payFailModalVariantRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }


    class PayfailDataViewHolder extends RecyclerView.ViewHolder {

        PayFailModalVariantRecyclerAdapter payFailModalVariantRecyclerAdapter;
        TextView modelName;
        RecyclerView variantRecyclerView;
        public PayfailDataViewHolder(@NonNull View itemView) {
            super(itemView);
            modelName = itemView.findViewById(R.id.user_claim_textview_modal_name);
            variantRecyclerView=itemView.findViewById(R.id.user_claim_recycleview_variant);
        }
    }
}
