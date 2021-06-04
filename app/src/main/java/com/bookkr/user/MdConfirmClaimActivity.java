package com.bookkr.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ScreenSlidePagerAdapter;
import com.adapter.UserClaimConfirmSiteDataRecyclerAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fragment.UserClaimSiteDataFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.model.ModalVariant;
import com.model.PayFailData;
import com.model.RequestParameter;
import com.model.SiteData;
import com.model.UserClaim;
import com.model.confirmclaim.ConfirmClaimDataResponse;
import com.preferences.ShPrefUserDetails;
import com.retrofit.APIClient;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MdConfirmClaimActivity extends FragmentActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private TextView claim_details_textview_claim_quantity, claim_details_textview_confirm_claim, claim_details_textview_left_quantity;
    private RecyclerView claim_confirm_mirecycleview_sites, claim_confirm_flipkartrecycleview_sites;
    private LinearLayout mi_store,flipkart;

    private ProgressDialog progress;
    private final String TAG = "UserClaimDetails";
    private int requestFor = -1;
    private boolean claimConfirmUpdated = false;
    String claimedId,reqId,totalquantity;
    private static MdConfirmClaimActivity activity;
    private UserClaim userClaim;
    private SiteData selectedSiteData;

    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_confirm_claim);
        Log.d("serajdata","md activity open");
        try {


            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            claim_details_textview_claim_quantity = findViewById(R.id.claim_details_textview_claim_quantity);
            claim_details_textview_confirm_claim = findViewById(R.id.claim_details_textview_confirm_claim);
            claim_details_textview_left_quantity = findViewById(R.id.claim_details_textview_left_quantity);
            claim_confirm_mirecycleview_sites = findViewById(R.id.claim_confirm_mirecycleview_sites);
            claim_confirm_flipkartrecycleview_sites = findViewById(R.id.claim_confirm_flipkartrecycleview_sites);
            mi_store = findViewById(R.id.mi_store);
            flipkart = findViewById(R.id.flipkart);

            mPager = (ViewPager) findViewById(R.id.pager);

            claimedId = getIntent().getExtras().getString("claim_requirement_id");
            reqId = getIntent().getExtras().getString("requirement_id");
            totalquantity = getIntent().getExtras().getString("total");
            Log.d("serajdata","ucca req id "+reqId+ " cid "+claimedId);


            ShPrefUserDetails.setStringData(this,"totalquantity",totalquantity);

            mi_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPager.setCurrentItem(0,true);
                   
                    Toast.makeText(MdConfirmClaimActivity.this, "Mi Store", Toast.LENGTH_SHORT).show();
                }
            });

            flipkart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("serajdata","flipkart clicked");
                    mPager.setCurrentItem(1,true);

                    Toast.makeText(MdConfirmClaimActivity.this, "Flipkart Store", Toast.LENGTH_SHORT).show();
                }
            });

            activity = this;

            //getDataFromApi();
            getDataFromAPiSimple();

        } catch (Exception e) {
            Log.d("serajdata","error "+e.getLocalizedMessage());
            //giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getDataFromAPiSimple() {
        Log.d("serajsimpleapi","Api Called");
        final String token = ShPrefUserDetails.getToken(this);
        String id= reqId;
        String id2=claimedId;
        Log.d("serajsimpleapi","path "+id+" "+id2);
        Call<ConfirmClaimDataResponse> call = APIClient.getInterface().getConfirmClaimData(token,id,id2);
        try {
               call.enqueue(new Callback<ConfirmClaimDataResponse>() {
                   @Override
                   public void onResponse(Call<ConfirmClaimDataResponse> call, retrofit2.Response<ConfirmClaimDataResponse> response) {
                       Log.d("serajsimpleapi","response recieved se"+response.isSuccessful());
                       Log.d("serajsimpleapi","response recieved "+response.message());
                       ConfirmClaimDataResponse confirmClaimDataResponse=response.body();
                       Log.d("serajsimpleapi","parsing data");
                       if (confirmClaimDataResponse.getStatus()==200){
                           pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),confirmClaimDataResponse.getData());
                           mPager.setAdapter(pagerAdapter);
                       }
                   }

                   @Override
                   public void onFailure(Call<ConfirmClaimDataResponse> call, Throwable t) {
                       Log.d("serajsimpleapi","response failed "+t.getLocalizedMessage());
                   }
               });
        }catch (Exception e){
            Log.d("serajsimpleapi","response failed "+e.getLocalizedMessage());
        }
    }

    private void getDataFromApi() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            requestFor = 1;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserConfirmClaimedDetails() + "/" + reqId + "/" + claimedId);
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);

            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri() + "?" + parameter.getEncodedParams(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (progress != null) {
                                    progress.dismiss();
                                }
                                Log.d("serajdata", "server response => " + response);

                                if (response != null) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.success)) {
                                        try {

                                            if (!jsonObject.isNull("data")) {
                                                JSONArray jsonDataArray = jsonObject.getJSONArray("data");
                                                for(int i=0;i<jsonDataArray.length();i++){
                                                    JsonObject jsonObject1= (JsonObject) jsonDataArray.get(i);


                                                }



                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
                    volleyError.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        Map<String, String> headers = super.getHeaders();
                        if (headers != null) {
                            if (headers.size() == 0) {
                                headers = new HashMap<>();
                            }
                        } else {
                            headers = new HashMap<>();
                        }

                        headers.put(AppURLParams.Authorization, token + "");
                        return headers;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return super.getHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(this, request);

        }catch (Exception e){

        }

    }

    private void giveErrorOnNoData() {
    }


    private void setDataSiteAdapter() {
        try {
            ArrayList<SiteData> siteDataArrayList = userClaim.getSiteDataArrayList();
            if (siteDataArrayList != null && siteDataArrayList.size() > 0) {
                setUnSelectRadioButton();
                siteDataArrayList.get(0).setChecked(true);
                claim_confirm_mirecycleview_sites.setVisibility(View.VISIBLE);

                UserClaimConfirmSiteDataRecyclerAdapter adapter = new UserClaimConfirmSiteDataRecyclerAdapter(this, siteDataArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                claim_confirm_mirecycleview_sites.setLayoutManager(linearLayoutManager);
                claim_confirm_mirecycleview_sites.setAdapter(adapter);
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
            UserClaimSiteDataFragment dataFragment = new UserClaimSiteDataFragment();
            fragmentTransaction.replace(R.id.container_FL, dataFragment, AppURLParams.fragmentSite).commit();

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public SiteData getSelectedSiteData() {
        return selectedSiteData;
    }

    public static MdConfirmClaimActivity getActivity() {
        return activity;
    }

    public void userConfirmClaim(View view) {
        try {
            Log.d("ConfirmClaim","Button Clicked");
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            AppUtils.hideKeyboard(this);

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Log.d("ConfirmClaim","Null User");
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            ArrayList<SiteData> siteDataArrayList = userClaim.getSiteDataArrayList();
            if (siteDataArrayList != null || siteDataArrayList.size() > 0) {

            } else {
                Log.d("ConfirmClaim","No Data");
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                return;
            }

            JSONArray siteJSONArray = new JSONArray();
            for (int i = 0; i < siteDataArrayList.size(); i++) {
                SiteData siteData = siteDataArrayList.get(i);
                JSONObject siteJSONObject = new JSONObject();
                siteJSONObject.put(AppURLParams.site_name, siteData.getSite_name() + "");
                siteJSONObject.put(AppURLParams.site_quantity, siteData.getTotal_quantity() + "");
                siteJSONObject.put(AppURLParams.claim_confirm_id, siteData.getClaim_confirm_id() + "");

                ArrayList<ModalVariant> siteVariantDataArrayList = siteData.getModalVariantArrayList();
                if (siteVariantDataArrayList != null && siteVariantDataArrayList.size() > 0) {
                    JSONArray variantJSONArray = new JSONArray();
                    for (int j = 0; j < siteVariantDataArrayList.size(); j++) {
                        ModalVariant modalVariant = siteVariantDataArrayList.get(j);
                        JSONObject variantJSONObject = new JSONObject();
                        variantJSONObject.put(AppURLParams.requirement_variant_id, modalVariant.getRequirement_variant_id() + "");
                        variantJSONObject.put(AppURLParams.claim_quantity_id, modalVariant.getClaim_quantity_id() + "");
                        variantJSONObject.put(AppURLParams.cod_quantity, modalVariant.getCod_quantity() + "");
                        variantJSONObject.put(AppURLParams.prepaid_quantity, modalVariant.getPrepaid_quantity() + "");
                        variantJSONObject.put(AppURLParams.payfail_quantity, modalVariant.getPayfail_quantity() + "");
                        variantJSONObject.put(AppURLParams.otp_quantity, modalVariant.getOtp_quantity() + "");
                        variantJSONArray.put(variantJSONObject);

                    }
                    siteJSONObject.put(AppURLParams.variant, variantJSONArray);

                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                    return;
                }

                PayFailData payFailData = siteData.getPayFailData();
                if (payFailData != null) {
                    JSONObject payFailJSONObject = new JSONObject();
                    payFailJSONObject.put(AppURLParams.payfail_details_id, payFailData.getPayfail_details_id() + "");
                    payFailJSONObject.put(AppURLParams.username, payFailData.getUsername() + "");
                    payFailJSONObject.put(AppURLParams.password, payFailData.getPassword() + "");
                    payFailJSONObject.put(AppURLParams.otp_send_on, payFailData.getOtp_send_on() + "");
                    payFailJSONObject.put(AppURLParams.whatsapp_no, payFailData.getWhatsapp_no() + "");
                    payFailJSONObject.put(AppURLParams.no_of_orders, payFailData.getNo_of_orders() + "");
                    payFailJSONObject.put(AppURLParams.total_amount, payFailData.getTotal_amount() + "");
                    payFailJSONObject.put(AppURLParams.time_left, payFailData.getTime_left() + "");
                    payFailJSONObject.put(AppURLParams.is_cod_available, payFailData.getIs_cod_available() + "");
                    siteJSONObject.put(AppURLParams.payfail_details, payFailJSONObject);

                } else {
                    JSONObject payFailJSONObject = new JSONObject();
                    payFailJSONObject.put(AppURLParams.payfail_details_id, "");
                    payFailJSONObject.put(AppURLParams.username, "");
                    payFailJSONObject.put(AppURLParams.password, "");
                    payFailJSONObject.put(AppURLParams.otp_send_on, "");
                    payFailJSONObject.put(AppURLParams.whatsapp_no, "");
                    payFailJSONObject.put(AppURLParams.no_of_orders, "");
                    payFailJSONObject.put(AppURLParams.total_amount, "");
                    payFailJSONObject.put(AppURLParams.time_left, "");
                    payFailJSONObject.put(AppURLParams.is_cod_available, "");
                    siteJSONObject.put(AppURLParams.payfail_details, payFailJSONObject);
                }

                siteJSONArray.put(siteJSONObject);
            }
            Log.d(TAG, "siteData\n" + siteJSONArray);

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getClaimConfirm());
            parameter.setParam(AppURLParams.event_id, userClaim.getEvent_id() + "");
            parameter.setParam(AppURLParams.claim_requirement_id, userClaim.getClaim_requirement_id() + "");
            parameter.setParam(AppURLParams.requirement_id, userClaim.getRequirement_id() + "");
            parameter.setParam(AppURLParams.siteData, siteJSONArray + "");
            Log.d("ConfirmClaim","Api Called");
            sendDataListData(parameter);

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendDataListData(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        Map<String, String> headers = super.getHeaders();
                        if (headers != null) {
                            if (headers.size() == 0) {
                                headers = new HashMap<>();
                            }
                        } else {
                            headers = new HashMap<>();
                        }

                        headers.put(AppURLParams.Authorization, token + "");
                        return headers;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return super.getHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1f));
            request.setShouldCache(false);
            GetServerData.addRequestToQueue(this, request);
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Log.d(TAG, "server response => " + response);

            if (response != null) {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.success)) {
                    processResponse(jsonObject);

                } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();

                } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            if (progress != null)
                progress.dismiss();
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            volleyError.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processResponse(JSONObject jsonObject) {
        try {
            claimConfirmUpdated = true;
            redirectUpdateUser();

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void redirectUpdateUser() {
        try {
            claimConfirmUpdated = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ack_data_saved);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        activity = null;
        userClaim = null;
        setUnSelectRadioButton();

        if (claimConfirmUpdated) {
            setResult(71);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 81) {
                setDataSiteAdapter();
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