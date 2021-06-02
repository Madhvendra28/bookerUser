package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.BankDetails;
import com.utils.AppURLParams;

import java.util.ArrayList;

public class BankDetailsListAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<BankDetails> list;

    public BankDetailsListAdapter(Activity activity, ArrayList<BankDetails> list) {
        super(activity, R.layout.adapter_bank_details_item, list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_bank_details_item, parent, false);

        try {
            final BankDetails bankDetails = list.get(position);
            TextView adapter_textview_account_type = rowView.findViewById(R.id.adapter_textview_account_type);
            LinearLayout adapter_LL_upi_id = rowView.findViewById(R.id.adapter_LL_upi_id);
            TextView adapter_textview_upi_id = rowView.findViewById(R.id.adapter_textview_upi_id);

            LinearLayout adapter_LL_bank = rowView.findViewById(R.id.adapter_LL_bank);
            TextView adapter_textview_bank_name = rowView.findViewById(R.id.adapter_textview_bank_name);
            TextView adapter_textview_account_name = rowView.findViewById(R.id.adapter_textview_account_name);
            TextView adapter_textview_account_number = rowView.findViewById(R.id.adapter_textview_account_number);
            TextView adapter_textview_ifsc_code = rowView.findViewById(R.id.adapter_textview_ifsc_code);
            TextView adapter_textview_branch = rowView.findViewById(R.id.adapter_textview_branch);

//            ImageView adapter_imageview_edit = rowView.findViewById(R.id.adapter_imageview_edit);
//            ImageView adapter_imageview_delete = rowView.findViewById(R.id.adapter_imageview_delete);
            ImageView adapter_imageview_share = rowView.findViewById(R.id.adapter_imageview_share);


            if (bankDetails.getAccount_type().equals(AppURLParams.statusVal1)) {
                adapter_textview_account_type.setText(activity.getString(R.string.upi_details));
                adapter_LL_upi_id.setVisibility(View.VISIBLE);
                adapter_LL_bank.setVisibility(View.GONE);

                adapter_textview_upi_id.setText(bankDetails.getUpi_id() + "");

            } else if (bankDetails.getAccount_type().equals(AppURLParams.statusVal2)) {
                adapter_textview_account_type.setText(activity.getString(R.string.bank_details));
                adapter_LL_upi_id.setVisibility(View.GONE);
                adapter_LL_bank.setVisibility(View.VISIBLE);

                adapter_textview_bank_name.setText(bankDetails.getBank_name() + "");
                adapter_textview_account_name.setText(bankDetails.getAccount_name() + "");
                adapter_textview_account_number.setText(bankDetails.getAccount_number() + "");
                adapter_textview_ifsc_code.setText(bankDetails.getIfsc_code() + "");
                adapter_textview_branch.setText(bankDetails.getBranch_name() + "");
            }

            adapter_imageview_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareBankDetails(bankDetails);
                }
            });

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
            return rowView;
        }
    }

    private void shareBankDetails(BankDetails bankDetails) {
        try {

            String message = "";
            if (bankDetails.getAccount_type().equals(AppURLParams.statusVal1)) {
                message = activity.getString(R.string.upi_details) + ": " + bankDetails.getUpi_id();

            } else if (bankDetails.getAccount_type().equals(AppURLParams.statusVal2)) {
                message = activity.getString(R.string.bank_details) + "\n" +
                        activity.getString(R.string.bank_name) + ": " + bankDetails.getBank_name() + "\n" +
                        activity.getString(R.string.account_name) + ": " + bankDetails.getAccount_name() + "\n" +
                        activity.getString(R.string.account_number) + ": " + bankDetails.getAccount_number() + "\n" +
                        activity.getString(R.string.ifsc_code) + ": " + bankDetails.getIfsc_code() + "\n" +
                        activity.getString(R.string.branch) + ": " + bankDetails.getBranch_name();
            }

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}