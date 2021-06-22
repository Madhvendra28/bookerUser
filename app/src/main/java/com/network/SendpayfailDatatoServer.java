package com.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.UserClaimPayFailDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;

import com.model.payfailModel.VariantDatum;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.GetServerData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SendpayfailDatatoServer {

    Context context;
    List<VariantDatum> variantDatumList;
    UserClaimPayFailDetailsActivity activity;
    String claimConfirmId,requirement_id,username,password,otpsendon,whatsappno,number_of_order,total_amoumt,time_left,is_cod_available,pay_fail_model_id,requirement_model_id,model_name;
    boolean respnce;


    public SendpayfailDatatoServer(Context context, List<VariantDatum> variantDatumList, UserClaimPayFailDetailsActivity activity, String claimConfirmId, String requirement_id, String username, String password, String otpsendon, String whatsappno, String number_of_order, String total_amoumt, String time_left, String is_cod_available, String pay_fail_model_id, String requirement_model_id, String model_name) {
        this.context = context;
        this.variantDatumList = variantDatumList;
        this.activity = activity;
        this.claimConfirmId = claimConfirmId;
        this.requirement_id = requirement_id;
        this.username = username;
        this.password = password;
        this.otpsendon = otpsendon;
        this.whatsappno = whatsappno;
        this.number_of_order = number_of_order;
        this.total_amoumt = total_amoumt;
        this.time_left = time_left;
        this.is_cod_available = is_cod_available;
        this.pay_fail_model_id = pay_fail_model_id;
        this.requirement_model_id = requirement_model_id;
        this.model_name = model_name;
        this.respnce=false;
    }

    public boolean sendPayFailData(){

        if (variantDatumList.equals(null)){
            return false;
        }


        try {
            Log.d("serajpayfailsubmit","Method called");

            JSONArray siteJSONArray = new JSONArray();
            JSONObject siteJSONObject = new JSONObject();
            siteJSONObject.put("pay_fail_model_id", pay_fail_model_id + "");
            siteJSONObject.put("requirement_model_id", requirement_model_id + "");
            siteJSONObject.put("model_name", model_name );


            List<VariantDatum> siteVariantDataArrayList =variantDatumList;
            if (siteVariantDataArrayList != null && siteVariantDataArrayList.size() > 0) {
                JSONArray variantJSONArray = new JSONArray();
                for (int j = 0; j < siteVariantDataArrayList.size(); j++) {
                    VariantDatum modalVariant = siteVariantDataArrayList.get(j);
                    JSONObject variantJSONObject = new JSONObject();
                    variantJSONObject.put("requirement_variant_id", modalVariant.getRequirementVariantId() + "");
                    variantJSONObject.put("pay_fail_variant_id", modalVariant.getVariantId());
                    variantJSONObject.put("variant_name", modalVariant.getVariantName() + "");
                    variantJSONObject.put("variant_price", modalVariant.getVariantPrice() + "");
                    variantJSONObject.put("quantity", modalVariant.getPayFailQuantity() + "");

                    Log.d("serajpayfailsubmit",""+modalVariant.getVariantName());
                    Log.d("serajpayfailsubmit",""+modalVariant.getVariantPrice());
                    Log.d("serajpayfailsubmit",""+modalVariant.getPayFailQuantity());
                    Log.d("serajpayfailsubmit",""+modalVariant.getRequirementVariantId());


                    variantJSONArray.put(variantJSONObject);

                }
                siteJSONObject.put(AppURLParams.variant, variantJSONArray);

            } else {
                Toast.makeText(context, "No data Found", Toast.LENGTH_SHORT).show();

            }



            siteJSONArray.put(siteJSONObject);

            Log.d("serajpayfailsubmit", "siteData\n" + siteJSONArray.length());



            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getPayfailSubmit());
            parameter.setParam("claim_confirm_id", claimConfirmId + "");
            parameter.setParam("total_amount", total_amoumt + "");
            parameter.setParam("time_left", time_left + "");
            parameter.setParam("is_cod_available",   "no");

            parameter.setParam("requirement_id" , requirement_id+ "");
            parameter.setParam("username", username + "");
            parameter.setParam("password" , password+ "");
            parameter.setParam("otp_send_on" , otpsendon + "");
            parameter.setParam("whatsapp_no" , whatsappno + "");
            parameter.setParam("no_of_orders" , number_of_order + "");
            parameter.setParam(AppURLParams.siteData, siteJSONArray + "");
            Log.d("serajpayfailsubmit",parameter.toString());
            Log.d("serajpayfailsubmit","Api is  called");
            //sendDataListData(parameter);
            return sendDataListData(parameter);
        } catch (Exception e) {
            CoordinatorLayout coordinatorLayout=new CoordinatorLayout(context);
            Snackbar.make(coordinatorLayout,"Error while sending data to server ", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }




        return true;
    }
    private boolean sendDataListData(RequestParameter parameter) {
        Log.d("serajpayfailsubmit","calling api");
        try {
            Log.d("serajsss", parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    respnce = true;
                    Log.d("serajpayfailsubmit", "Responce recieved "+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("serajpayfailsubmit","volley error "+error.getLocalizedMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("serajpayfailsubmit","Param build");
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
                        Log.d("serajpayfailsubmit","token "+token);
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
            respnce=false;
            e.printStackTrace();
        }

        return respnce;
    }
}