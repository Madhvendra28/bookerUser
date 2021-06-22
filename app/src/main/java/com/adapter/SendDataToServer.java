package com.adapter;

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
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.confirmclaim.MdSiteData;
import com.model.confirmclaim.Variant;
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

public class SendDataToServer {
    Context context;
    MdSiteData mdSiteData;
    FragmentActivity activity;
    boolean respnce;

    public SendDataToServer(Context context, MdSiteData mdSiteData, FragmentActivity activity) {
        this.context = context;
        this.mdSiteData = mdSiteData;
        this.activity = activity;
        this.respnce = false;
    }

    public boolean sendCCData(){

        if (mdSiteData.equals(null)){
            return false;
        }


        try {
            Log.d("ConfirmClaim","Method called");

            JSONArray siteJSONArray = new JSONArray();
            JSONObject siteJSONObject = new JSONObject();
            siteJSONObject.put(AppURLParams.site_name, mdSiteData.getSite_name() + "");
            siteJSONObject.put(AppURLParams.site_quantity, mdSiteData.getTotal_quantity() + "");
            siteJSONObject.put(AppURLParams.claim_confirm_id, 0 );

            Log.d("mdData",mdSiteData.getSite_name());
            Log.d("mdData",mdSiteData.getTotal_quantity());
            Log.d("mdData",mdSiteData.getClaim_confirm_id());


            List<Variant> siteVariantDataArrayList = mdSiteData.getModalVariantArrayList();
                if (siteVariantDataArrayList != null && siteVariantDataArrayList.size() > 0) {
                    JSONArray variantJSONArray = new JSONArray();
                    for (int j = 0; j < siteVariantDataArrayList.size(); j++) {
                        Variant modalVariant = siteVariantDataArrayList.get(j);
                        JSONObject variantJSONObject = new JSONObject();
                        variantJSONObject.put(AppURLParams.requirement_variant_id, modalVariant.getRequirementVariantId() + "");
                        variantJSONObject.put(AppURLParams.claim_quantity_id, 0);
                        variantJSONObject.put("cod", modalVariant.getCod() + "");
                        variantJSONObject.put("prepaid", modalVariant.getPrePaid() + "");
                        variantJSONObject.put("requirement_model_id", mdSiteData.getRequirement_model_id() + "");
                       // variantJSONObject.put(AppURLParams.payfail_quantity, modalVariant.getPayFail() + "");
                        //variantJSONObject.put(AppURLParams.otp_quantity, "");
                        variantJSONArray.put(variantJSONObject);
                        Log.d("mdData",modalVariant.getRequirementVariantId().toString());
                        Log.d("mdData",modalVariant.getCod().toString());
                        Log.d("mdData",mdSiteData.getRequirement_model_id());

                    }
                    siteJSONObject.put("variant_data", variantJSONArray);

                } else {
                    Toast.makeText(context, "No data Found", Toast.LENGTH_SHORT).show();

                }

                Log.d("mdData",siteJSONObject.toString());

                siteJSONArray.put(siteJSONObject);

            Log.d("ConfirmClaim", "siteData\n" + siteJSONArray);



            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getClaimConfirm());
            parameter.setParam("claim_requirement_id", mdSiteData.getClaim_confirm_id() + "");
            parameter.setParam("requirement_id", mdSiteData.getRequirement_model_id() + "");
            parameter.setParam("data_list", siteJSONArray +"");
            Log.d("myparameters",parameter.toString());
            Log.d("ConfirmClaim","Api is  called");
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
        Log.d("ConfirmClaim","calling api");
        try {
            Log.d("serajsss", parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    respnce = true;
                    Log.d("ConfirmClaim", "Responce recieved "+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ConfirmClaim","volley error "+error.getStackTrace());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("ConfirmClaim","Param build");
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
                        Log.d("ConfirmClaim","token "+token);
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
