package com.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkr.user.R;
import com.model.confirmclaim.Variant;
import com.preferences.ShPrefUserDetails;

import java.util.List;

public class MdClaimConfirmAdapter extends RecyclerView.Adapter<MdClaimConfirmAdapter.MyHolder>{

    List<Variant> variantList;
    Context context;

    public MdClaimConfirmAdapter(List<Variant> variantList, Context context) {
        this.variantList = variantList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.claim_varient_data,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.variantName.setText(""+variantList.get(position).getVariantName());
//        holder.cod.setText(""+variantList.get(position).getCod());
//        holder.prePaid.setText(""+variantList.get(position).getPrePaid());
//        holder.payFail.setText(""+variantList.get(position).getPayFail());
        String totalQuantity= ShPrefUserDetails.getStringData("totalquantity",context);

        try {


            holder.cod.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count>0){
                        int pp = TextUtils.isEmpty(holder.prePaid.getText()) ? 0 : Integer.parseInt(holder.prePaid.getText().toString());
                        int pf = TextUtils.isEmpty(holder.payFail.getText()) ? 0 : Integer.parseInt(holder.payFail.getText().toString());
                        if(Integer.parseInt(totalQuantity)<(Integer.parseInt(""+s)+pp+pf)){
                            holder.cod.setText("");
                            Toast.makeText(context, "Total quantity limit exceeds, should be less than "+totalQuantity, Toast.LENGTH_SHORT).show();
                        }else{
                            variantList.get(position).setCod(Integer.parseInt(""+s));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.payFail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count>0){
                        int pp = TextUtils.isEmpty(holder.prePaid.getText()) ? 0 : Integer.parseInt(holder.prePaid.getText().toString());
                        int pf = TextUtils.isEmpty(holder.cod.getText()) ? 0 : Integer.parseInt(holder.cod.getText().toString());
                        if(Integer.parseInt(totalQuantity)<(Integer.parseInt(""+s)+pp+pf)){
                            holder.payFail.setText("");
                            Toast.makeText(context, "Total quantity limit exceeds, should be less than "+totalQuantity, Toast.LENGTH_SHORT).show();
                        }else{
                            variantList.get(position).setPayFail(Integer.parseInt(""+s));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.prePaid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count>0){
                        int pp = TextUtils.isEmpty(holder.cod.getText()) ? 0 : Integer.parseInt(holder.cod.getText().toString());
                        int pf = TextUtils.isEmpty(holder.payFail.getText()) ? 0 : Integer.parseInt(holder.payFail.getText().toString());
                        if(Integer.parseInt(totalQuantity)<(Integer.parseInt(""+s)+pp+pf)){
                            holder.prePaid.setText("");
                            Toast.makeText(context, "Total quantity limit exceeds, should be less than "+totalQuantity, Toast.LENGTH_SHORT).show();
                        }else{
                            variantList.get(position).setPrePaid(Integer.parseInt(""+s));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });




        }catch (Exception e){
            Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView variantName;
        EditText cod,prePaid,payFail;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            variantName=itemView.findViewById(R.id.varient_name);
            cod=itemView.findViewById(R.id.cod);
            prePaid=itemView.findViewById(R.id.prepaid);
            payFail=itemView.findViewById(R.id.payfail);
        }
    }

    public List<Variant> getUpdatedData(){
        return variantList;
    }
}
