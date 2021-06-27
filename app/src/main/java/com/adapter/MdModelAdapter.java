package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.PayFailModalVariantRecyclerAdapter;
import com.bookkr.user.R;
import com.bookkr.user.UserClaimPayFailDetailsActivity;
import com.model.UserClaim;
import com.model.payfailModel.ModelDatum;

import java.util.List;

public class MdModelAdapter extends RecyclerView.Adapter<MdModelAdapter.MyHolder> {

    UserClaimPayFailDetailsActivity activity;

    List<ModelDatum> modelDatumList;

    public MdModelAdapter(UserClaimPayFailDetailsActivity activity, List<ModelDatum> modelDatumList) {
        this.activity = activity;
        this.modelDatumList = modelDatumList;
        Log.d("mdpayfailrv","initialized adapter");

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.md_model_pay_fail_list_items,parent,false);
        Log.d("mdpayfailrv","view holder");
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Log.d("mdpayfailrv","model name "+modelDatumList.get(position).getModelName());

        holder.modelName.setText(modelDatumList.get(position).getModelName());


//        holder.payFailModalVariantRecyclerAdapter = new PayFailModalVariantRecyclerAdapter(activity,modelDatumList.get(position).getVariantData());
//        holder.variantRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//        holder.variantRecyclerView.setAdapter(holder.payFailModalVariantRecyclerAdapter);

    }

    @Override
    public int getItemCount() {
        Log.d("mdpayfailrv","item count ");
        return modelDatumList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView modelName;
       // RecyclerView variantRecyclerView;
        PayFailModalVariantRecyclerAdapter payFailModalVariantRecyclerAdapter;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("mdpayfailrv","subclass");
            modelName = itemView.findViewById(R.id.user_claim_textview_modal_name);
           // variantRecyclerView = itemView.findViewById(R.id.user_claim_recycleview_variant);
        }
    }
}
