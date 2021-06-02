package com.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.adapter.UserClaimModalVariantRecyclerAdapter;
import com.bookkr.user.R;
import com.bookkr.user.UserClaimConfirmActivity;
import com.bookkr.user.UserClaimHistoryListActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.ModalVariant;
import com.model.SiteData;
import com.model.UserClaim;

import java.util.ArrayList;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimSiteDataFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private EditText fragment_claim_edittext_quantity_for_site;
    private TextView fragment_claim_textview_modal_name;
    private RecyclerView fragment_claim_recycleview_variant;

    private View view;
    public static final String TAG = "UserClaimSiteData";

    private UserClaimConfirmActivity activity;
    private UserClaim userClaim;
    private SiteData selectedSiteData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            activity = (UserClaimConfirmActivity) getActivity();
            view = inflater.inflate(R.layout.fragment_user_claim_site_data, container, false);
            userClaim = UserClaimHistoryListActivity.getActivity().getSelectedUserClaim();
            selectedSiteData = activity.getSelectedSiteData();

            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
            fragment_claim_edittext_quantity_for_site = view.findViewById(R.id.fragment_claim_edittext_quantity_for_site);
            fragment_claim_textview_modal_name = view.findViewById(R.id.fragment_claim_textview_modal_name);
            fragment_claim_recycleview_variant = view.findViewById(R.id.fragment_claim_recycleview_variant);


            fragment_claim_edittext_quantity_for_site.setText(selectedSiteData.getTotal_quantity() + "");
            fragment_claim_textview_modal_name.setText(userClaim.getModel_name() + "");
            fragment_claim_edittext_quantity_for_site.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    String string = s.toString();
                    selectedSiteData.setTotal_quantity(string);
                }
            });

            ArrayList<ModalVariant> variantArrayList = selectedSiteData.getModalVariantArrayList();
            if (variantArrayList != null && variantArrayList.size() > 0) {
                fragment_claim_recycleview_variant.setVisibility(View.VISIBLE);

                fragment_claim_recycleview_variant.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                UserClaimModalVariantRecyclerAdapter adapter = new UserClaimModalVariantRecyclerAdapter(activity, selectedSiteData, variantArrayList);
                fragment_claim_recycleview_variant.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                fragment_claim_recycleview_variant.setVisibility(View.GONE);
                Snackbar.make(coordinatorLayout, activity.getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            }

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void giveErrorOnNoData() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnError() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}