package com.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.MdClaimConfirmAdapter;
import com.adapter.SendDataToServer;
import com.bookkr.user.MdConfirmClaimActivity;
import com.bookkr.user.R;
import com.bookkr.user.UserClaimPayFailDetailsActivity;
import com.model.confirmclaim.Datum;
import com.model.confirmclaim.MdSiteData;
import com.model.confirmclaim.Variant;
import com.preferences.ShPrefUserDetails;

import java.util.List;


public class MiStoreFragment extends Fragment implements View.OnClickListener {

    List<Datum> dataList;
    List<Variant> variantList;
    TextView dealerName,modelName,quantity;
    RecyclerView crv;
    MdClaimConfirmAdapter adapter;
    View view;
    String requiremenr_id,claim_id,totalQuantity;
    Button button;

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
        button=view.findViewById(R.id.cnfmbtn);
        crv.setLayoutManager(new LinearLayoutManager(getContext()));
        view.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                List<Variant> variants= adapter.getUpdatedData();
                Intent intent = new Intent(getContext(), UserClaimPayFailDetailsActivity.class);
                startActivity(intent);
            }
        });


        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getSiteName().equalsIgnoreCase("Amazon")){
                variantList=dataList.get(i).getModel().get(0).getVariant();
                dealerName.setText(""+dataList.get(i).getDealerName());
                modelName.setText(""+dataList.get(i).getModel().get(0).getModelName());

                break;
            }
        }

        totalQuantity= ShPrefUserDetails.getStringData("totalquantity",getContext());
        claim_id=ShPrefUserDetails.getStringData("confirmclaimid",getContext());
        requiremenr_id=ShPrefUserDetails.getStringData("requirementid",getContext());

        quantity.setText("Quantity left : "+totalQuantity);
        //variantList=dataList.get(0).getModel().get(0).getVariant();
        if (variantList.size() > 0){
            setData();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        return view;
    }

    private void sendData() {
        Log.d("ConfirmClaim","inside send data");
        List<Variant> variants= adapter.getUpdatedData();
        MdSiteData mdSiteData=new MdSiteData(requiremenr_id,"Amazon",totalQuantity,claim_id,variants);
        SendDataToServer sendDataToServer = new SendDataToServer(getContext(),mdSiteData,getActivity());

        boolean status=sendDataToServer.sendCCData();
        if(status){
            Toast.makeText(getContext(), "Data successfully send to server", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Failed to send data", Toast.LENGTH_SHORT).show();
        }

    }


    private void setData() {
      adapter=new MdClaimConfirmAdapter(variantList,getContext());
      crv.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.update:
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                List<Variant> variants= adapter.getUpdatedData();
                Intent intent = new Intent(getContext(), UserClaimPayFailDetailsActivity.class);
                startActivity(intent);

                break;
            default:
                Toast.makeText(getContext(), "Please provide id first", Toast.LENGTH_SHORT).show();
        }
    }
}