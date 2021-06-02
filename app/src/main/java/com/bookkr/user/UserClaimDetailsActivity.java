package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adapter.PosrReqTextLinkRecyclerAdapter;
import com.adapter.PostReqModalVariantRecyclerAdapter;
import com.adapter.RequirementAddressesRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.model.Address;
import com.model.ModalVariant;
import com.model.TextLink;
import com.model.UserClaim;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimDetailsActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView claim_details_recyclerview_variants, claim_details_recyclerview_address, claim_details_recyclerview_links;

    private TextView claim_details_textview_dealer_name, claim_details_textview_event_name, claim_details_textview_site_name,
            claim_details_textview_sale_start, claim_details_textview_sale_end, claim_details_textview_sale_type,
            claim_details_textview_modal_name, claim_details_textview_required_quantity, claim_details_textview_claim_quantity,
            claim_details_textview_payment_on, claim_details_textview_dealer_can_pay, claim_details_textview_compensation_for_rto,
            claim_details_textview_compensation_amount, claim_details_textview_precautions, claim_details_textview_dealer_note,
            claim_details_textview_claimed_quantity, claim_details_textview_confirm_quantity, claim_details_textview_failed_quantity,
            claim_details_textview_left_quantity;

    private ProgressDialog progress;
    private final String TAG = "UserClaimDetails";
    private int requestFor = -1;

    private boolean requirementClaimed = false;
    private UserClaim userClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_details);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            claim_details_recyclerview_variants = findViewById(R.id.claim_details_recyclerview_variants);
            claim_details_recyclerview_address = findViewById(R.id.claim_details_recyclerview_address);
            claim_details_recyclerview_links = findViewById(R.id.claim_details_recyclerview_links);

            claim_details_textview_dealer_name = findViewById(R.id.claim_details_textview_dealer_name);
            claim_details_textview_event_name = findViewById(R.id.claim_details_textview_event_name);
            claim_details_textview_site_name = findViewById(R.id.claim_details_textview_site_name);
            claim_details_textview_sale_start = findViewById(R.id.claim_details_textview_sale_start);
            claim_details_textview_sale_end = findViewById(R.id.claim_details_textview_sale_end);
            claim_details_textview_sale_type = findViewById(R.id.claim_details_textview_sale_type);
            claim_details_textview_modal_name = findViewById(R.id.claim_details_textview_modal_name);
            claim_details_textview_required_quantity = findViewById(R.id.claim_details_textview_required_quantity);
            claim_details_textview_claim_quantity = findViewById(R.id.claim_details_textview_claim_quantity);
            claim_details_textview_payment_on = findViewById(R.id.claim_details_textview_payment_on);
            claim_details_textview_dealer_can_pay = findViewById(R.id.claim_details_textview_dealer_can_pay);
            claim_details_textview_compensation_for_rto = findViewById(R.id.claim_details_textview_compensation_for_rto);
            claim_details_textview_compensation_amount = findViewById(R.id.claim_details_textview_compensation_amount);
            claim_details_textview_precautions = findViewById(R.id.claim_details_textview_precautions);
            claim_details_textview_dealer_note = findViewById(R.id.claim_details_textview_dealer_note);

            claim_details_textview_claimed_quantity = findViewById(R.id.claim_details_textview_claimed_quantity);
            claim_details_textview_confirm_quantity = findViewById(R.id.claim_details_textview_confirm_quantity);
            claim_details_textview_failed_quantity = findViewById(R.id.claim_details_textview_failed_quantity);
            claim_details_textview_left_quantity = findViewById(R.id.claim_details_textview_left_quantity);

//            userClaim = getIntent().getExtras().getParcelable(AppURLParams.claimDetailsObj);
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
                claim_details_textview_dealer_name.setText(userClaim.getDealer_name() + "");
                claim_details_textview_event_name.setText(userClaim.getEvent_name() + "");
                claim_details_textview_site_name.setText(userClaim.getSite_name() + "");
                claim_details_textview_sale_type.setText(userClaim.getSale_type() + "");
                claim_details_textview_modal_name.setText(userClaim.getModel_name() + "");
                claim_details_textview_required_quantity.setText(userClaim.getRequired_quantity() + "");
                claim_details_textview_claim_quantity.setText(userClaim.getClaim_quantity() + "");
                claim_details_textview_payment_on.setText(userClaim.getPayment_on() + "");
                claim_details_textview_dealer_can_pay.setText(userClaim.getCan_pay() + "");
                claim_details_textview_precautions.setText(userClaim.getPrecautions() + "");
                claim_details_textview_dealer_note.setText(userClaim.getNote() + "");

                claim_details_textview_sale_start.setText(AppUtils.convertDateWithoutTime(userClaim.getStart_date())
                        + " | " + AppUtils.getFormattedDateTime(userClaim.getStart_timing()));
                claim_details_textview_sale_end.setText(AppUtils.convertDateWithoutTime(userClaim.getEnd_date())
                        + " | " + AppUtils.getFormattedDateTime(userClaim.getEnd_timing()));

                if (userClaim.getRto().equals(AppURLParams.statusVal1)) {
                    claim_details_textview_compensation_for_rto.setText(getString(R.string.yes) + "");
                    claim_details_textview_compensation_amount.setText(userClaim.getRto_charges() + "");
                } else {
                    claim_details_textview_compensation_for_rto.setText(getString(R.string.no) + "");
                }

                ArrayList<ModalVariant> modalVariantArrayList = userClaim.getModalVariantArrayList();
                if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                    claim_details_recyclerview_variants.setVisibility(View.VISIBLE);
                    claim_details_recyclerview_variants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    PostReqModalVariantRecyclerAdapter adapter = new PostReqModalVariantRecyclerAdapter(this, modalVariantArrayList);
                    claim_details_recyclerview_variants.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    claim_details_recyclerview_variants.setVisibility(View.GONE);
                }

                ArrayList<Address> addressArrayList = userClaim.getAddressArrayList();
                if (addressArrayList != null && addressArrayList.size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < addressArrayList.size(); i++) {
                        Address address = addressArrayList.get(i);
                        list.add(address.getName() + " " + address.getSurname());
                    }

                    claim_details_recyclerview_address.setVisibility(View.VISIBLE);
                    claim_details_recyclerview_address.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    RequirementAddressesRecyclerAdapter adapter = new RequirementAddressesRecyclerAdapter(this, addressArrayList);
                    claim_details_recyclerview_address.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    claim_details_recyclerview_variants.setVisibility(View.GONE);
                }

                ArrayList<TextLink> textLinkArrayList = userClaim.getTextLinkArrayList();
                if (textLinkArrayList != null && textLinkArrayList.size() > 0) {
                    claim_details_recyclerview_links.setVisibility(View.VISIBLE);
                    claim_details_recyclerview_links.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    PosrReqTextLinkRecyclerAdapter adapter = new PosrReqTextLinkRecyclerAdapter(this, textLinkArrayList);
                    claim_details_recyclerview_links.setAdapter(adapter);

                } else {
                    claim_details_recyclerview_links.setVisibility(View.GONE);
                }

                try {
                    claim_details_textview_claimed_quantity.setText(userClaim.getQuantity() + "");
                    claim_details_textview_confirm_quantity.setText(userClaim.getConfirmed_quantity() + "");
                    claim_details_textview_failed_quantity.setText(userClaim.getFailed_quantity() + "");

                    int quantityClaimed = Integer.parseInt(userClaim.getQuantity());
                    int quantityConfirmed = Integer.parseInt(userClaim.getConfirmed_quantity());
                    int quantityFailed = Integer.parseInt(userClaim.getFailed_quantity());
                    int remaining = quantityClaimed - quantityConfirmed - quantityFailed;
                    claim_details_textview_left_quantity.setText(remaining + "");
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

    @Override
    public void onBackPressed() {
        userClaim = null;
        if (requirementClaimed) {
            setResult(51);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
           if (resultCode == 71) {
               requirementClaimed = true;
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
