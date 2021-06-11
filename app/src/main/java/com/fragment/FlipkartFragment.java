package com.fragment;

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
import com.bookkr.user.R;
import com.model.confirmclaim.Datum;
import com.model.confirmclaim.MdSiteData;
import com.model.confirmclaim.Variant;
import com.preferences.ShPrefUserDetails;

import java.util.ArrayList;
import java.util.List;


public class FlipkartFragment extends Fragment {

    List<Datum> dataList;
    List<Variant> variantList;
    TextView dealerName,modelName,quantity;
    RecyclerView crv;
    MdClaimConfirmAdapter adapter;
    View view;
    Button button;
    String requiremenr_id,claim_id,totalQuantity;

    public FlipkartFragment(List<Datum> dataList) {
        this.dataList = dataList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_flipkart, container, false);


        crv=view.findViewById(R.id.claim_rv);
        dealerName=view.findViewById(R.id.tv_dealer);
        quantity=view.findViewById(R.id.tv_quantity);
        modelName=view.findViewById(R.id.tv_modelname);
        button = view.findViewById(R.id.payfailButton);
        Log.d("serajdatadebug","ccdfdfse");
        crv.setLayoutManager(new LinearLayoutManager(getContext()));
        variantList = new ArrayList<>();
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getSiteName().equalsIgnoreCase("FlipKart")){
                variantList=dataList.get(i).getModel().get(0).getVariant();
                dealerName.setText(""+dataList.get(i).getDealerName());
                modelName.setText(""+dataList.get(i).getModel().get(0).getModelName());
                break;
            }
        }

        totalQuantity= ShPrefUserDetails.getStringData("totalquantity",getContext());
        quantity.setText("Quantity left : "+totalQuantity);
        claim_id = ShPrefUserDetails.getStringData("confirmclaimid",getContext());
        requiremenr_id = ShPrefUserDetails.getStringData("requirementid",getContext());
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
        MdSiteData mdSiteData=new MdSiteData(requiremenr_id,"Flipkart",totalQuantity,claim_id,variants);
        SendDataToServer sendDataToServer = new SendDataToServer(getContext(),mdSiteData,getActivity());

        boolean status=sendDataToServer.sendCCData();
        if(!status){
            Toast.makeText(getContext(), "Data successfully send to server", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Failed to send data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        adapter=new MdClaimConfirmAdapter(variantList,getContext());
        crv.setAdapter(adapter);
    }
}