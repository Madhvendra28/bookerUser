package com.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.R;
import com.bookkr.user.RequirementListActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.fragment.app.DialogFragment;

/**
 * Created by ANKIT on 27-Jul-20.
 */
public class RequirementClaimDialogFragment extends DialogFragment {

    private TextView fragment_claim_textview_requirement_id, fragment_claim_textview_required_quantity, fragment_claim_textview_claim_qty, fragment_claim_textview_total_qty;
    private EditText fragment_claim_edittext_new_qty, fragment_claim_edittext_total_amount;
    private Spinner fragment_claim_spinner_payment_method;
    private LinearLayout fragment_claim_LL_total_amount;
    private CheckBox fragment_claim_checkxox_can_pay;
    private Button fragment_claim_button_claim;

    private ProgressDialog progress;
    private Activity activity;

    public static final String TAG = "ClaimDialog";
    private String requirement_id, event_id, quantity, claimed_quantity, callingFrom;
    private boolean reqClaimed = false;

    public RequirementClaimDialogFragment(String requirement_id, String event_id, String quantity, String claimed_quantity, String callingFrom) {
        this.requirement_id = requirement_id;
        this.event_id = event_id;
        this.quantity = quantity;
        this.claimed_quantity = claimed_quantity;
        this.callingFrom = callingFrom;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            activity = getActivity();
            View view = inflater.inflate(R.layout.fragment_requirement_claim_dialog, container, false);

            fragment_claim_textview_requirement_id = view.findViewById(R.id.fragment_claim_textview_requirement_id);
            fragment_claim_textview_required_quantity = view.findViewById(R.id.fragment_claim_textview_required_quantity);
            fragment_claim_textview_claim_qty = view.findViewById(R.id.fragment_claim_textview_claim_qty);
            fragment_claim_textview_total_qty = view.findViewById(R.id.fragment_claim_textview_total_qty);
            fragment_claim_edittext_new_qty = view.findViewById(R.id.fragment_claim_edittext_new_qty);
            fragment_claim_edittext_total_amount = view.findViewById(R.id.fragment_claim_edittext_total_amount);
            fragment_claim_spinner_payment_method = view.findViewById(R.id.fragment_claim_spinner_payment_method);
            fragment_claim_LL_total_amount = view.findViewById(R.id.fragment_claim_LL_total_amount);
            fragment_claim_checkxox_can_pay = view.findViewById(R.id.fragment_claim_checkxox_can_pay);
            fragment_claim_button_claim = view.findViewById(R.id.fragment_claim_button_claim);

            fragment_claim_textview_requirement_id.setText("#" + requirement_id + "");
            fragment_claim_textview_required_quantity.setText(quantity + "");
            fragment_claim_textview_claim_qty.setText(claimed_quantity + "");

            if(!quantity.equals("") && !claimed_quantity.equals("")){
                Integer reqQty = Integer.parseInt(quantity);
                Integer claimedQty = Integer.parseInt(claimed_quantity);
                int leftQty = reqQty - claimedQty;
                fragment_claim_textview_total_qty.setText("/ " + leftQty + "");
            }

            fragment_claim_checkxox_can_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        fragment_claim_LL_total_amount.setVisibility(View.VISIBLE);
                        fragment_claim_edittext_total_amount.setText("");
                    } else {
                        fragment_claim_LL_total_amount.setVisibility(View.GONE);
                        fragment_claim_edittext_total_amount.setText("");
                    }
                }
            });

            fragment_claim_button_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int leftQty = 0;
                        if(!quantity.equals("") && !claimed_quantity.equals("")){
                            Integer reqQty = Integer.parseInt(quantity);
                            Integer claimedQty = Integer.parseInt(claimed_quantity);
                            leftQty = reqQty - claimedQty;

                        }else{
                            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        String newQuantity = fragment_claim_edittext_new_qty.getText() + "";
                        if (newQuantity.equals("")) {
                            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_user_quantity), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        Integer userQuantity = Integer.parseInt(newQuantity);
//                        Integer reqQuantity = Integer.parseInt(quantity);
                        if (userQuantity > leftQty) {
                            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_less_req_quantity), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        claimUserQuantity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void claimUserQuantity() {
        try {
            if (!ConnectionManager.isOnline(activity)) {
                ConnectionManager.createDialog(activity);
                Log.d("Network state", ConnectionManager.isOnline(activity) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(activity);
            if (userId == null) {
                Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            String newQuantity = fragment_claim_edittext_new_qty.getText() + "";
            String paymentMethod = fragment_claim_spinner_payment_method.getSelectedItem().toString();
            boolean isCanPayOther = fragment_claim_checkxox_can_pay.isChecked();
            String totalAmount = fragment_claim_edittext_total_amount.getText() + "";

            if (newQuantity.equals("")) {
                Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_user_quantity), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (paymentMethod.equals("")) {
                Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_select_payment_mode), Snackbar.LENGTH_LONG).show();
                return;
            }

            String canPayForOther = "";
            if (isCanPayOther) {
                if (totalAmount.equals("")) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_amount), Snackbar.LENGTH_LONG).show();
                    return;
                }

                canPayForOther = AppURLParams.statusVal1;
            } else {
                canPayForOther = AppURLParams.statusVal0;
            }

            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserClaimEvent());
            parameter.setParam(AppURLParams.requirement_id, requirement_id + "");
            parameter.setParam(AppURLParams.event_id, event_id + "");
            parameter.setParam(AppURLParams.quantity, newQuantity + "");
            parameter.setParam(AppURLParams.payment_method, paymentMethod + "");
            parameter.setParam(AppURLParams.is_pay_for_other, canPayForOther + "");
            parameter.setParam(AppURLParams.pay_amount, totalAmount + "");

            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(),
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
                                            reqClaimed = true;
                                            redirectToList();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(activity.findViewById(R.id.coordinatorLayout), jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
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

                    volleyError.printStackTrace();
                    giveErrorOnError();
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
            GetServerData.addRequestToQueue(activity, request);

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void redirectToList() {
        try {
            reqClaimed = true;
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.ack_data_saved), Snackbar.LENGTH_SHORT).show();
            if (callingFrom.equals(AppURLParams.statusVal1)) {
                ((RequirementListActivity) activity).refreshData();

            } else if (callingFrom.equals(AppURLParams.statusVal2)) {

            }

            this.dismiss();

        } catch (Exception e) {
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnError() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}