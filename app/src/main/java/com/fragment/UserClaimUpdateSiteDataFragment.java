package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adapter.UserClaimUpdateModalVariantRecyclerAdapter;
import com.bookkr.user.R;
import com.bookkr.user.UserClaimHistoryListActivity;
import com.bookkr.user.UserClaimUpdateDetailsActivity;
import com.bookkr.user.UserClaimUpdateShippingActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.ModalVariant;
import com.model.SiteData;
import com.model.UserClaim;
import com.utils.AppURLParams;

import java.util.ArrayList;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimUpdateSiteDataFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private TextView fragment_claim_textview_modal_name;
    private RecyclerView fragment_claim_recycleview_variant;
    private Button fragment_button_update_shipping;

    private View view;
    public static final String TAG = "UserClaimSiteData";

    private UserClaimUpdateDetailsActivity activity;
    private UserClaim userClaim;
    private SiteData selectedSiteData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            activity = (UserClaimUpdateDetailsActivity) getActivity();
            view = inflater.inflate(R.layout.fragment_user_claim_update_site_data, container, false);
            userClaim = UserClaimHistoryListActivity.getActivity().getSelectedUserClaim();
            selectedSiteData = activity.getSelectedSiteData();

            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
//            LinearLayout fragment_claim_LL_quantity_for_site = view.findViewById(R.id.fragment_claim_LL_quantity_for_site);
//            fragment_claim_edittext_quantity_for_site = view.findViewById(R.id.fragment_claim_edittext_quantity_for_site);
            fragment_claim_textview_modal_name = view.findViewById(R.id.fragment_claim_textview_modal_name);
            fragment_claim_recycleview_variant = view.findViewById(R.id.fragment_claim_recycleview_variant);
            fragment_button_update_shipping = view.findViewById(R.id.fragment_button_update_shipping);

//            fragment_claim_LL_quantity_for_site.setVisibility(View.GONE);
            fragment_claim_textview_modal_name.setText(userClaim.getModel_name() + "");

            ArrayList<ModalVariant> variantArrayList = selectedSiteData.getModalVariantArrayList();
            if (variantArrayList != null && variantArrayList.size() > 0) {
                fragment_claim_recycleview_variant.setVisibility(View.VISIBLE);
                fragment_button_update_shipping.setVisibility(View.VISIBLE);

                fragment_claim_recycleview_variant.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                UserClaimUpdateModalVariantRecyclerAdapter adapter = new UserClaimUpdateModalVariantRecyclerAdapter(activity, variantArrayList);
                fragment_claim_recycleview_variant.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                fragment_claim_recycleview_variant.setVisibility(View.GONE);
                fragment_button_update_shipping.setVisibility(View.GONE);
                giveErrorOnNoData();
            }

            fragment_button_update_shipping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(activity, UserClaimUpdateShippingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(AppURLParams.site_name, selectedSiteData.getSite_name());
                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent, 80);
                        activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                    } catch (Exception e) {
                        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), activity.getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            return view;
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
            return null;
        }
    }

    public void giveErrorOnNoData() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnError() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}