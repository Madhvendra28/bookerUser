package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adapter.UserClaimUpdateDetailsSiteDataAdapter;
import com.fragment.UserClaimUpdateSiteDataFragment;
import com.google.android.material.snackbar.Snackbar;
import com.model.SiteData;
import com.model.UserClaim;
import com.utils.AppURLParams;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimUpdateDetailsActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextView claim_details_textview_claimed_quantity, claim_details_textview_confirm_quantity, claim_details_textview_failed_quantity,
            claim_details_textview_left_quantity;
    private RecyclerView claim_details_recycleview_sites;

    private ProgressDialog progress;
    private final String TAG = "RequirementDetails";
    private int requestFor = -1;

    private boolean bookingFailed = false;
    private UserClaim userClaim;
    private SiteData selectedSiteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_update_details);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            claim_details_textview_claimed_quantity = findViewById(R.id.claim_details_textview_claimed_quantity);
            claim_details_textview_confirm_quantity = findViewById(R.id.claim_details_textview_confirm_quantity);
            claim_details_textview_failed_quantity = findViewById(R.id.claim_details_textview_failed_quantity);
            claim_details_textview_left_quantity = findViewById(R.id.claim_details_textview_left_quantity);
            claim_details_recycleview_sites = findViewById(R.id.claim_details_recycleview_sites);

            userClaim = UserClaimHistoryListActivity.getActivity().getSelectedUserClaim();
            if (userClaim == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                return;
            } else {
                setDataInViews();
            }

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (userClaim != null) {

                try {
                    claim_details_textview_claimed_quantity.setText(userClaim.getQuantity() + "");
                    claim_details_textview_confirm_quantity.setText(userClaim.getConfirmed_quantity() + "");
                    claim_details_textview_failed_quantity.setText(userClaim.getFailed_quantity() + "");

                    int quantityClaimed = Integer.parseInt(userClaim.getQuantity());
                    int quantityConfirmed = Integer.parseInt(userClaim.getConfirmed_quantity());
                    int quantityFailed = Integer.parseInt(userClaim.getFailed_quantity());
                    int remaining = quantityClaimed - quantityConfirmed - quantityFailed;
                    claim_details_textview_left_quantity.setText(remaining + "");

                    setDataSiteAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_user_profile), Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataSiteAdapter() {
        try {
            ArrayList<SiteData> siteDataArrayList = userClaim.getSiteDataArrayList();
            if (siteDataArrayList != null && siteDataArrayList.size() > 0) {
                setUnSelectRadioButton();
                siteDataArrayList.get(0).setChecked(true);
                claim_details_recycleview_sites.setVisibility(View.VISIBLE);

                UserClaimUpdateDetailsSiteDataAdapter adapter = new UserClaimUpdateDetailsSiteDataAdapter(this, siteDataArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                claim_details_recycleview_sites.setLayoutManager(linearLayoutManager);
                claim_details_recycleview_sites.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public void setFragmentForSite(SiteData siteData) {
        try {
            selectedSiteData = siteData;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            UserClaimUpdateSiteDataFragment dataFragment = new UserClaimUpdateSiteDataFragment();
            fragmentTransaction.replace(R.id.claim_details_container_FL, dataFragment, AppURLParams.fragmentSite).commit();

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public SiteData getSelectedSiteData() {
        return selectedSiteData;
    }

    private void setUnSelectRadioButton() {
        try {
            ArrayList<SiteData> siteDataArrayList = userClaim.getSiteDataArrayList();
            for (int i = 0; i < siteDataArrayList.size(); i++) {
                siteDataArrayList.get(i).setChecked(false);
            }
        } catch (Exception e) {
        }
    }

    public void userConfirmClaim(View view) {
        try {
            Intent intent = new Intent(this, UserClaimConfirmActivity.class);
            startActivityForResult(intent, 70);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void userFailedClaim(View view) {
        try {
            Intent intent = new Intent(this, UserClaimFailedActivity.class);
            startActivityForResult(intent, 62);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (bookingFailed) {
            setResult(61);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 63) {
                bookingFailed = true;
                onBackPressed();

            } else if (resultCode == 71) {
                bookingFailed = true;
                onBackPressed();

            }else if (resultCode == 81) {
                bookingFailed = true;
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }
}
