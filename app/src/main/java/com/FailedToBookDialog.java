package com;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.R;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FailedToBookDialog extends Dialog {
    private Activity activity;
    private EditText user_claim_edittext_quantity, user_claim_edittext_reason;
    TextView tvQuantityLeftToUpdate;

    private ProgressDialog progress;
    private int requestFor = -1, clickedFor = 0;
    private boolean bookingFailed = false;

    String claimRequirementID = "";
    String failedQuantity = "";
    String failedQuantityReason = "";
    String remainingQuantity = "";

    OnRequestDoneListener listener;

    Button user_claim_button_save;


    private final String TAG = "FailedToBookDialog";

    public FailedToBookDialog(Activity activity, String claimRequirementID, String failedQuantity, String failedQuantityReason, String remainingQuantity, OnRequestDoneListener listener) {
        super(activity);
        this.activity = activity;
        this.claimRequirementID = claimRequirementID;
        this.failedQuantity = failedQuantity;
        this.failedQuantityReason = failedQuantityReason;
        this.remainingQuantity = remainingQuantity;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_failed_to_book);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        user_claim_edittext_quantity = findViewById(R.id.user_claim_edittext_quantity);
        user_claim_edittext_reason = findViewById(R.id.user_claim_edittext_reason);
        user_claim_button_save = findViewById(R.id.user_claim_button_save);
        tvQuantityLeftToUpdate = findViewById(R.id.tvQuantityLeftToUpdate);

        user_claim_edittext_quantity.setText(failedQuantity + "");
        user_claim_edittext_reason.setText(failedQuantityReason == null ? "" : failedQuantityReason);
        tvQuantityLeftToUpdate.setText(remainingQuantity);

        user_claim_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFailedData();
            }
        });

    }
    public void saveFailedData() {
        try {
            if (!ConnectionManager.isOnline(activity)) {
                Log.d("Network state", ConnectionManager.isOnline(activity) + "");
                ConnectionManager.createDialog(activity);
                return;
            }

            String userId = ShPrefUserDetails.getToken(activity);
            if (userId == null) {
                Toast.makeText(activity, activity.getString(R.string.error_user_id_not_found), Toast.LENGTH_SHORT).show();
              //  Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            AppUtils.hideKeyboard(activity);
            user_claim_edittext_quantity.setError(null);
            user_claim_edittext_reason.setError(null);

            String quantity = user_claim_edittext_quantity.getText() + "";
            String reason = user_claim_edittext_reason.getText().toString();

            if (quantity.equals("")) {
                Toast.makeText(activity, activity.getString(R.string.error_empty_quantity), Toast.LENGTH_SHORT).show();
                //Snackbar.make(coordinatorLayout, getString(R.string.error_empty_quantity), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_quantity.setError(activity.getString(R.string.error_empty_quantity));
                user_claim_edittext_quantity.requestFocus();
                return;
            }

            Integer claimQuantityInt = Integer.parseInt(remainingQuantity);
            Integer failedQuantityInt = Integer.parseInt(quantity);
            if (failedQuantityInt > claimQuantityInt) {
                Toast.makeText(activity, activity.getString(R.string.error_claim_failed_quantity_mismatch), Toast.LENGTH_SHORT).show();
               // Snackbar.make(coordinatorLayout, getString(R.string.error_claim_failed_quantity_mismatch), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_quantity.setError(activity.getString(R.string.error_claim_failed_quantity_mismatch));
                user_claim_edittext_quantity.requestFocus();
                return;
            }

            if (reason.equals("")) {
                Toast.makeText(activity, activity.getString(R.string.error_empty_reason), Toast.LENGTH_SHORT).show();
                //Snackbar.make(coordinatorLayout, getString(R.string.error_empty_reason), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_reason.setError( activity.getString(R.string.error_empty_reason));
                user_claim_edittext_reason.requestFocus();
                return;
            }

            progress = new ProgressDialog(activity);
            progress.setMessage(activity.getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getBookingFail());
            parameter.setParam(AppURLParams.claim_requirement_id, claimRequirementID + "");
            parameter.setParam(AppURLParams.fail_quantity, quantity + "");
            parameter.setParam(AppURLParams.fail_quantity_reason, reason + "");
            sendDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
           // Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendDataListData(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), new Response.Listener<String>() {
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
                                Toast.makeText(activity, jsonObject.getString(AppURLParams.message), Toast.LENGTH_SHORT).show();
                                //Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();

                            } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                Toast.makeText(activity, jsonObject.getString(AppURLParams.message), Toast.LENGTH_SHORT).show();
//                                Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        if (progress != null)
                            progress.dismiss();
                        Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
//                        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (progress != null) {
                            progress.dismiss();
                        }
                        Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
                       // Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                        error.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 1, 1f));
            request.setShouldCache(false);
            GetServerData.addRequestToQueue(activity, request);
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
            //Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            bookingFailed = true;
            listener.onRequestDone();
            dismiss();
            //redirectClaimList();
        } catch (Exception e) {
            Toast.makeText(activity, activity.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
       //     Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public interface OnRequestDoneListener{
        void onRequestDone();
    }
}
