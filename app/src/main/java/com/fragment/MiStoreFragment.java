package com.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adapter.MdClaimConfirmAdapter;
import com.bookkr.user.R;
import com.model.confirmclaim.Datum;
import com.model.confirmclaim.Variant;
import com.preferences.ShPrefUserDetails;

import java.util.List;


public class MiStoreFragment extends Fragment {

    List<Datum> dataList;
    List<Variant> variantList;
    TextView dealerName,modelName,quantity;
    RecyclerView crv;
    MdClaimConfirmAdapter adapter;
    View view;

    public MiStoreFragment(List<Datum> dataList) {
        this.dataList = dataList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_mi_store, container, false);

        crv=view.findViewById(R.id.claim_rv);
        dealerName=view.findViewById(R.id.tv_dealer);
        quantity=view.findViewById(R.id.tv_quantity);
        modelName=view.findViewById(R.id.tv_modelname);
        Log.d("serajdatadebug","ccdfdfse");
        crv.setLayoutManager(new LinearLayoutManager(getContext()));

        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getSiteName().equalsIgnoreCase("Amazon")){
                variantList=dataList.get(i).getModel().get(0).getVariant();
                dealerName.setText(""+dataList.get(i).getDealerName());
                modelName.setText(""+dataList.get(i).getModel().get(0).getModelName());
                break;
            }
        }

        String totalQuantity= ShPrefUserDetails.getStringData("totalquantity",getContext());
        quantity.setText("Quantity left : "+totalQuantity);
        //variantList=dataList.get(0).getModel().get(0).getVariant();
        if (variantList.size() > 0){
            setData();
        }


        return view;
    }

    private void setData() {
      adapter=new MdClaimConfirmAdapter(variantList,getContext());
      crv.setAdapter(adapter);
    }
}