package com.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookkr.user.OrderHistoryDetailsActivity;
import com.bookkr.user.R;
import com.model.ModalVariant;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ShipOrderHistoryModalVariantAdapter extends RecyclerView.Adapter<ShipOrderHistoryModalVariantAdapter.ViewHolder> {

    private OrderHistoryDetailsActivity activity;
    private ArrayList<String> stringArrayList;
    private ArrayList<ModalVariant> modalVariantArrayList;
    private ModalVariant selectedModalVariant;

    public ShipOrderHistoryModalVariantAdapter(OrderHistoryDetailsActivity activity, ArrayList<String> strings, ArrayList<ModalVariant> modalVariantArrayList) {
        this.activity = activity;
        this.stringArrayList = strings;
        this.modalVariantArrayList = modalVariantArrayList;
    }

    @Override
    public ShipOrderHistoryModalVariantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ship_order_history_modal_variant_item, parent, false);
        return new ShipOrderHistoryModalVariantAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView adapter_textview_vaiant_name, adapter_textview_vaiant_color, adapter_textview_vaiant_price,
                adapter_textview_vaiant_quantity, adapter_textview_advance_paid;

        public ViewHolder(View itemView) {
            super(itemView);

            adapter_textview_vaiant_name = itemView.findViewById(R.id.adapter_textview_vaiant_name);
            adapter_textview_vaiant_color = itemView.findViewById(R.id.adapter_textview_vaiant_color);
            adapter_textview_vaiant_price = itemView.findViewById(R.id.adapter_textview_vaiant_price);
            adapter_textview_vaiant_quantity = itemView.findViewById(R.id.adapter_textview_vaiant_quantity);
            adapter_textview_advance_paid = itemView.findViewById(R.id.adapter_textview_advance_paid);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final ModalVariant modalVariant = modalVariantArrayList.get(position);
            selectedModalVariant = modalVariant;

            holder.adapter_textview_vaiant_name.setText(modalVariant.getVariant_name() + "");
            holder.adapter_textview_vaiant_color.setText(modalVariant.getSelected_color() + "");
            holder.adapter_textview_vaiant_price.setText(activity.getString(R.string.rupees) + " " + modalVariant.getVariant_price() + "");
            holder.adapter_textview_vaiant_quantity.setText(modalVariant.getShipping_quantity() + "");
            holder.adapter_textview_advance_paid.setText(activity.getString(R.string.rupees) + " " + modalVariant.getAdvance_paid() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
