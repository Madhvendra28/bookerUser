package com.bookkr.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FailedToBookDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClaimUserDetailActivity extends AppCompatActivity {

    TextView tvClaimedQuantity,tvConfirmedQuantity,tvFailedToBook,tvReasons,tvQuantityLeftToUpdate;
    private CoordinatorLayout coordinatorLayout;
    private int requestFor = -1;
    private ProgressDialog progress;
    String claimedId;
    private LinearLayout requirement_list_LL_container, nodata_LL;
    private final String TAG = "Requirement List";
    private TextView nodata_textview;
    Activity activity;

    MaterialButton btnConfirmQuantity, btnFailedAndUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_user_detail);

        activity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        claimedId = getIntent().getExtras().getString("claim_requirement_id");

        nodata_LL = findViewById(R.id.nodata_LL);
        nodata_textview = findViewById(R.id.nodata_textview);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        tvClaimedQuantity = findViewById(R.id.tvClaimedQuantity);
        tvConfirmedQuantity = findViewById(R.id.tvConfirmedQuantity);
        tvFailedToBook = findViewById(R.id.tvFailedToBook);
        tvReasons = findViewById(R.id.tvReasons);
        tvQuantityLeftToUpdate = findViewById(R.id.tvQuantityLeftToUpdate);
        btnConfirmQuantity = findViewById(R.id.btnConfirmQuantity);
        btnFailedAndUpdate = findViewById(R.id.btnFailedAndUpdate);
        getRequirementList();

        btnFailedAndUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FailedToBookDialog dialog = new FailedToBookDialog(activity, claimedId, tvFailedToBook.getText().toString(),
                        tvReasons.getText().toString(), tvQuantityLeftToUpdate.getText().toString(), new FailedToBookDialog.OnRequestDoneListener() {
                    @Override
                    public void onRequestDone() {
                        getRequirementList();
                    }
                });
                dialog.show();
            }
        });

        btnConfirmQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirm = new Intent(ClaimUserDetailActivity.this,UserClaimConfirmActivity.class);
                startActivity(confirm);
            }
        });
    }

    private void getRequirementList() {
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
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserClaimedDetails()+"/"+claimedId);
           /* if (event_id != null) {
                parameter.setParam(AppURLParams.event_id, event_id);
            } else {
                parameter.setParam(AppURLParams.event_id, "");
            }*/

            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

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
                                Log.d(TAG, "server response => " + response);

                                if (response != null) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.success)) {
                                        try {

                                            if (!jsonObject.isNull("data")) {
                                                JSONObject jsonArray = jsonObject.getJSONObject("data");
                                                tvClaimedQuantity.setText(!jsonArray.isNull("claimed_quantity") ? jsonArray.getString("claimed_quantity") : "");
                                                tvConfirmedQuantity.setText(!jsonArray.isNull("confirmed_quantity") ? jsonArray.getString("confirmed_quantity") : "");
                                                tvFailedToBook.setText(!jsonArray.isNull("failed_to_book") ? jsonArray.getString("failed_to_book") : "");
                                                tvReasons.setText(!jsonArray.isNull("fail_quantity_reason") ? jsonArray.getString("fail_quantity_reason") : "");
                                                tvQuantityLeftToUpdate.setText(!jsonArray.isNull("remaining_quantity") ? jsonArray.getString("remaining_quantity") : "");

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

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }
    private void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            requirement_list_LL_container.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_no_data_found));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void giveErrorOnError() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            requirement_list_LL_container.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}