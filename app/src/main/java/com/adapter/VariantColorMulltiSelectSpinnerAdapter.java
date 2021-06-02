package com.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bookkr.user.R;
import com.model.Address;

import java.util.ArrayList;

public class VariantColorMulltiSelectSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList<String> list, selectedItems;
    private ModalVariantRecyclerViewAdapter adapter;
    private VariantColorMulltiSelectSpinnerAdapter myAdapter;
    private boolean isFromView = false;

    public VariantColorMulltiSelectSpinnerAdapter(Activity activity, ModalVariantRecyclerViewAdapter adapter, ArrayList<String> list, ArrayList<String> selectedItems) {
        super(activity, R.layout.adapter_spinner_item_checkbox, list);
        this.activity = activity;
        this.adapter = adapter;

        this.list = list;
        this.selectedItems = selectedItems;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        try {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(activity);
                convertView = layoutInflator.inflate(R.layout.adapter_spinner_item_checkbox, null);

                holder = new ViewHolder();
                holder.mTextView = convertView.findViewById(R.id.adapter_textview_item_name);
                holder.mCheckBox = convertView.findViewById(R.id.adapter_checkbox_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String modalString = list.get(position);
            Log.d("view for", modalString + ", " + selectedItems.contains(modalString));

            // To check weather checked event fire from getview() or user input
            isFromView = true;

            holder.mTextView.setText(modalString + "");

            if (selectedItems.contains(modalString)) {
                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }

            isFromView = false;

            holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int getPosition = (Integer) buttonView.getTag();

                    if (!isFromView) {
                        Log.e("isFromView", !isFromView + "");
                        if (isChecked) {
                            if(!selectedItems.contains(modalString)){
                             selectedItems.add(modalString);
                            }
                        } else {
                            if(selectedItems.contains(modalString)){
                                selectedItems.remove(modalString);
                            }
                        }
                        adapter.setSelectedColorModalVariantItems(selectedItems);
                    myAdapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }

}
