package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.MoneyRequest;
import com.utils.AppURLParams;

import java.util.ArrayList;

public class MoneyRequestHistoryListAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<MoneyRequest> list;

    public MoneyRequestHistoryListAdapter(Activity activity, ArrayList<MoneyRequest> list) {
        super(activity, R.layout.adapter_money_request_history_item, list);
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
        View rowView = inflater.inflate(R.layout.adapter_money_request_history_item, parent, false);

        try {
            final MoneyRequest moneyRequest = list.get(position);
            TextView adapter_textview_date = rowView.findViewById(R.id.tvDate);
            TextView adapter_textview_amount = rowView.findViewById(R.id.adapter_textview_amount);
            TextView adapter_textview_transaction_id = rowView.findViewById(R.id.adapter_textview_transaction_id);

            LinearLayout adapter_LL_message = rowView.findViewById(R.id.adapter_LL_message);
            TextView adapter_textview_message = rowView.findViewById(R.id.adapter_textview_message);

            LinearLayout adapter_LL_status = rowView.findViewById(R.id.adapter_LL_status);
            TextView adapter_textview_status = rowView.findViewById(R.id.adapter_textview_status);

            adapter_textview_date.setText(moneyRequest.getCreate_date() + "");
            adapter_textview_amount.setText(activity.getString(R.string.rupees) + " " + moneyRequest.getAmount() + "");
            adapter_textview_transaction_id.setText(moneyRequest.getTransaction_id() + "");

            if (moneyRequest.getStatus().equalsIgnoreCase(AppURLParams.statusVal0)) {
                adapter_textview_status.setText(activity.getString(R.string.status_pending) + "");
                adapter_LL_status.setBackgroundColor(activity.getResources().getColor(R.color.bg_color_grey));

            } else if (moneyRequest.getStatus().equalsIgnoreCase(AppURLParams.statusVal1)) {
                adapter_textview_status.setText(activity.getString(R.string.status_success) + "");
                adapter_LL_status.setBackgroundColor(activity.getResources().getColor(R.color.bg_color_blue));

            } else if (moneyRequest.getStatus().equalsIgnoreCase(AppURLParams.statusVal2)) {
                adapter_textview_status.setText(activity.getString(R.string.status_order_cancelled) + "");
                adapter_LL_status.setBackgroundColor(activity.getResources().getColor(R.color.bg_color_red));

            } else {
                adapter_textview_status.setText(activity.getString(R.string.na) + "");
            }

            if (moneyRequest.getMessage().equals("")) {
                adapter_LL_message.setVisibility(View.GONE);
            } else {
                adapter_LL_message.setVisibility(View.VISIBLE);
                adapter_textview_message.setText(moneyRequest.getMessage() + "");
            }

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
            return rowView;
        }
    }
}