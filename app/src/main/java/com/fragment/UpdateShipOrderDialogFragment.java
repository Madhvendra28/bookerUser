package com.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.bookkr.user.OrderHistoryActivity;
import com.bookkr.user.OrderHistoryDetailsActivity;
import com.bookkr.user.R;
import com.bookkr.user.RequirementListActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.ShipOrderDetails;
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
public class UpdateShipOrderDialogFragment extends DialogFragment {

    private TextView fragment_textview_requirement_id, fragment_textview_status;
    private Spinner fragment_spinner_status;
    private Button fragment_button_update_shipping;

    private LinearLayout fragment_LL_ofd_details;
    private EditText fragment_edittext_courier_person_mobile, fragment_edittext_pin, fragment_edittext_otp_for_delivery,
            fragment_edittext_comment;
    private CheckBox fragment_checkxox_is_pay_online;


    private ProgressDialog progress;
    private Activity activity;

    public static final String TAG = "UpdateShipOrderDialog";
    private boolean shipUpdated = false;
    private ShipOrderDetails shipOrderDetails;
    private String callingFrom;
    private int prevStatusPosition;


    public UpdateShipOrderDialogFragment(String callingFrom) {
        super();
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
            shipOrderDetails = OrderHistoryActivity.getActivity().getSelectedShipOrderDetails();
            View view = null;

            if (shipOrderDetails != null) {
                view = inflater.inflate(R.layout.fragment_update_ship_order_dialog, container, false);

                fragment_textview_requirement_id = view.findViewById(R.id.fragment_textview_requirement_id);
                fragment_textview_status = view.findViewById(R.id.fragment_textview_status);
                fragment_spinner_status = view.findViewById(R.id.fragment_spinner_status);
                fragment_button_update_shipping = view.findViewById(R.id.fragment_button_update_shipping);

                fragment_LL_ofd_details = view.findViewById(R.id.fragment_LL_ofd_details);
                fragment_edittext_courier_person_mobile = view.findViewById(R.id.fragment_edittext_courier_person_mobile);
                fragment_edittext_pin = view.findViewById(R.id.fragment_edittext_pin);
                fragment_edittext_otp_for_delivery = view.findViewById(R.id.fragment_edittext_otp_for_delivery);
                fragment_edittext_comment = view.findViewById(R.id.fragment_edittext_comment);
                fragment_checkxox_is_pay_online = view.findViewById(R.id.fragment_checkxox_is_pay_online);


                fragment_textview_requirement_id.setText("#" + shipOrderDetails.getRequirement_id() + "");

                String status = shipOrderDetails.getStatus();
                if (status.equalsIgnoreCase(AppURLParams.statusVal0)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_gray));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorGrey));
                    fragment_textview_status.setText(activity.getString(R.string.status_shipped) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal1)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                    fragment_textview_status.setText(activity.getString(R.string.status_reached_dc) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal2)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                    fragment_textview_status.setText(activity.getString(R.string.status_ofd) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal3)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                    fragment_textview_status.setText(activity.getString(R.string.status_undelivered) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal4)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                    fragment_textview_status.setText(activity.getString(R.string.status_rejected) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal5)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_primary));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorPrimary));
                    fragment_textview_status.setText(activity.getString(R.string.status_rto) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal6)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_green));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorGreen));
                    fragment_textview_status.setText(activity.getString(R.string.status_delivered) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal7)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_red));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorRed));
                    fragment_textview_status.setText(activity.getString(R.string.status_donot_accept) + "");

                } /*else if (status.equalsIgnoreCase(AppURLParams.statusVal8)) {
                    fragment_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_red));
                    fragment_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorRed));
                    fragment_textview_status.setText(activity.getString(R.string.status_missing_info) + "");

                }*/ else {
                    fragment_textview_status.setText(activity.getString(R.string.na) + "");
                }

                try {
                    if (status.equals("")) {
                        prevStatusPosition = Integer.parseInt(status);
                        fragment_spinner_status.setSelection(prevStatusPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fragment_spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (position == 2) {
                                fragment_LL_ofd_details.setVisibility(View.VISIBLE);

                            } else {
                                fragment_LL_ofd_details.setVisibility(View.GONE);
                            }

                            fragment_edittext_courier_person_mobile.setText("");
                            fragment_edittext_pin.setText("");
                            fragment_edittext_otp_for_delivery.setText("");
                            fragment_edittext_comment.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                fragment_button_update_shipping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String statusPosition = (fragment_spinner_status.getSelectedItemPosition()) + "";
                            String courierMobile = fragment_edittext_courier_person_mobile.getText() + "";
                            String pin = fragment_edittext_pin.getText() + "";
                            String otpForDelivery = fragment_edittext_otp_for_delivery.getText() + "";
                            String comment = fragment_edittext_comment.getText() + "";
                            String isPayOnline = "";

                            if (statusPosition.equals(AppURLParams.statusVal2)) {
                                if (courierMobile.equals("")) {
                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_courier_mobile_no), Snackbar.LENGTH_LONG).show();
                                    return;
                                }

                                if (courierMobile.length() < 10) {
                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_invalid_mobile), Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                if (pin.equals("")) {
                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_delivery_pin), Snackbar.LENGTH_LONG).show();
                                    return;
                                }

                                if (otpForDelivery.equals("")) {
                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_delivery_otp), Snackbar.LENGTH_LONG).show();
                                    return;
                                }

//                                if (comment.equals("")) {
//                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_comment), Snackbar.LENGTH_LONG).show();
//                                    return;
//                                }

                                if (fragment_checkxox_is_pay_online.isChecked()) {
                                    isPayOnline = AppURLParams.statusVal1;
                                } else {
                                    isPayOnline = AppURLParams.statusVal0;
                                }

                                if (isPayOnline.equals("")) {
                                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_select_available_for_online_pay), Snackbar.LENGTH_LONG).show();
                                    return;
                                }
                            }

                            updateShipOrderStatus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }/*else{
                view = inflater.inflate(R.layout.fragment_update_ship_order_dialog, container, false);
            }*/

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateShipOrderStatus() {
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

            String statusPosition = (fragment_spinner_status.getSelectedItemPosition()) + "";
            String courierMobile = fragment_edittext_courier_person_mobile.getText() + "";
            String pin = fragment_edittext_pin.getText() + "";
            String otpForDelivery = fragment_edittext_otp_for_delivery.getText() + "";
            String comment = fragment_edittext_comment.getText() + "";
            String isPayOnline = "";

            if (statusPosition.equals(AppURLParams.statusVal2)) {
                if (courierMobile.equals("")) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_courier_mobile_no), Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (courierMobile.length() < 10) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_invalid_mobile), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (pin.equals("")) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_delivery_pin), Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (otpForDelivery.equals("")) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_delivery_otp), Snackbar.LENGTH_LONG).show();
                    return;
                }

//                if (comment.equals("")) {
//                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_empty_comment), Snackbar.LENGTH_LONG).show();
//                    return;
//                }

                if (fragment_checkxox_is_pay_online.isChecked()) {
                    isPayOnline = AppURLParams.statusVal1;
                } else {
                    isPayOnline = AppURLParams.statusVal0;
                }

                if (isPayOnline.equals("")) {
                    Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.error_select_available_for_online_pay), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getShippingStatusUpdate());
            parameter.setParam(AppURLParams.shipping_details_id, shipOrderDetails.getShipping_details_id() + "");
            parameter.setParam(AppURLParams.status, statusPosition + "");
            parameter.setParam(AppURLParams.courier_boy_no, courierMobile + "");
            parameter.setParam(AppURLParams.pin, pin + "");
            parameter.setParam(AppURLParams.otp_for_delivery, otpForDelivery + "");
            parameter.setParam(AppURLParams.comment, comment + "");
            parameter.setParam(AppURLParams.is_online_pay, isPayOnline + "");

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
                                            shipUpdated = true;
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
            shipUpdated = true;
            Snackbar.make(activity.findViewById(R.id.coordinatorLayout), getString(R.string.ack_data_saved), Snackbar.LENGTH_SHORT).show();

            if (callingFrom.equals(AppURLParams.statusVal1)) {
                ((OrderHistoryActivity) activity).refreshData();

            } else if (callingFrom.equals(AppURLParams.statusVal2)) {
                ((OrderHistoryDetailsActivity) activity).redirectUserProfile();
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