package com.bookkr.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.adapter.UpdateShippingModalVariantAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.ModalVariant;
import com.model.RequestParameter;
import com.model.SiteData;
import com.model.UserClaim;
import com.preferences.ShPrefUserDetails;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.ImageProcessing;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserClaimUpdateShippingActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,
        AdapterView.OnItemSelectedListener {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView update_shipping_recyclerview_variants;
    private Spinner update_shipping_spinner_payment_mode;

    private TextView update_shipping_textview_event, update_shipping_textview_required_quantity, update_shipping_textview_claim_quantity,
            update_shipping_textview_confirm_quantity, update_shipping_textview_dealer_name, update_shipping_textview_order_date,
            update_shipping_textview_modal_name;

    private EditText update_shipping_edittext_name_on_order, update_shipping_edittext_order_value,
            update_shipping_edittext_courier, update_shipping_edittext_tracking_id, update_shipping_edittext_tracking_link,
            update_shipping_edittext_address, update_shipping_edittext_secret_note;
    private ImageView update_shipping_imageview_address_image, update_shipping_imageview_address_image_close;
    private Button update_shipping_button_expected_date;

    private ProgressDialog progress;
    private final String TAG = "UpdateShipping";
    private int requestFor = -1;

    private File root;
    private File mypath;
    private File imageAddress;
    private ImageView captureImageForImageView, captureImageForCloseImageView;
    private boolean shippingUpdated = false, updatingAddressImage = false;

    private SiteData selectedSiteData;
    private UserClaim userClaim;
    private String expectedDate = "", paymentMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_update_shipping);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            update_shipping_recyclerview_variants = findViewById(R.id.update_shipping_recyclerview_variants);

            update_shipping_textview_event = findViewById(R.id.update_shipping_textview_event);
            update_shipping_textview_required_quantity = findViewById(R.id.update_shipping_textview_required_quantity);
            update_shipping_textview_claim_quantity = findViewById(R.id.update_shipping_textview_claim_quantity);
            update_shipping_textview_confirm_quantity = findViewById(R.id.update_shipping_textview_confirm_quantity);
            update_shipping_textview_dealer_name = findViewById(R.id.update_shipping_textview_dealer_name);
            update_shipping_textview_order_date = findViewById(R.id.update_shipping_textview_order_date);
            update_shipping_textview_modal_name = findViewById(R.id.update_shipping_textview_modal_name);

            update_shipping_button_expected_date = findViewById(R.id.update_shipping_button_expected_date);
            update_shipping_edittext_name_on_order = findViewById(R.id.update_shipping_edittext_name_on_order);
            update_shipping_edittext_order_value = findViewById(R.id.update_shipping_edittext_order_value);
            update_shipping_edittext_courier = findViewById(R.id.update_shipping_edittext_courier);
            update_shipping_edittext_tracking_id = findViewById(R.id.update_shipping_edittext_tracking_id);
            update_shipping_edittext_tracking_link = findViewById(R.id.update_shipping_edittext_tracking_link);
            update_shipping_edittext_address = findViewById(R.id.update_shipping_edittext_address);
            update_shipping_edittext_secret_note = findViewById(R.id.update_shipping_edittext_secret_note);

            update_shipping_spinner_payment_mode = findViewById(R.id.update_shipping_spinner_payment_mode);
            update_shipping_imageview_address_image = findViewById(R.id.update_shipping_imageview_address_image);
            update_shipping_imageview_address_image_close = findViewById(R.id.update_shipping_imageview_address_image_close);

            update_shipping_spinner_payment_mode.setOnItemSelectedListener(this);

            userClaim = UserClaimHistoryListActivity.getActivity().getSelectedUserClaim();
            if (userClaim == null) {
                giveErrorOnError();
                return;
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String siteName = bundle.getString(AppURLParams.site_name);

                ArrayList<SiteData> siteDataArrayList = userClaim.getSiteDataArrayList();
                if (siteDataArrayList != null && siteDataArrayList.size() > 0) {
                    for (int i = 0; i < siteDataArrayList.size(); i++) {
                        SiteData siteData = siteDataArrayList.get(i);
                        if (siteData.getSite_name().equalsIgnoreCase(siteName)) {
                            selectedSiteData = siteData;
                            break;
                        }
                    }

                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                    return;
                }
            } else {
                giveErrorOnError();
                return;
            }

            if (selectedSiteData == null) {
                giveErrorOnNoData();
                return;
            }

            root = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (!root.exists()) {
                root.mkdirs();
            }

            if (userClaim != null && selectedSiteData != null) {
                setDataInViews();
            } else {
                giveErrorOnNoData();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void submitUpdateShipping(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            AppUtils.hideKeyboard(this);
            update_shipping_edittext_name_on_order.setError(null);
            update_shipping_edittext_order_value.setError(null);
            update_shipping_edittext_courier.setError(null);
            update_shipping_edittext_tracking_id.setError(null);
            update_shipping_edittext_tracking_link.setError(null);
            update_shipping_edittext_address.setError(null);
            update_shipping_edittext_secret_note.setError(null);

            String nameOnOrder = update_shipping_edittext_name_on_order.getText() + "";
            String orderValue = update_shipping_edittext_order_value.getText().toString();
            String courier = update_shipping_edittext_courier.getText() + "";
            String trackingId = update_shipping_edittext_tracking_id.getText() + "";
            String trackingLink = update_shipping_edittext_tracking_link.getText() + "";
            String address = update_shipping_edittext_address.getText() + "";
            String secretNote = update_shipping_edittext_secret_note.getText() + "";

            if (expectedDate.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_shipping_expected_date), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (nameOnOrder.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_name_on_order), Snackbar.LENGTH_SHORT).show();
                update_shipping_edittext_name_on_order.setError(getString(R.string.error_empty_name_on_order));
                update_shipping_edittext_name_on_order.requestFocus();
                return;
            }

            if (paymentMode.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_payment_mode), Snackbar.LENGTH_LONG).show();
                return;
            }

            ArrayList<ModalVariant> selectedModalVariantList = null;
            ArrayList<ModalVariant> modalVariantArrayList = selectedSiteData.getModalVariantArrayList();
            if (modalVariantArrayList != null) {

                selectedModalVariantList = new ArrayList<>();
                for (int i = 0; i < modalVariantArrayList.size(); i++) {
                    ModalVariant modalVariant = modalVariantArrayList.get(i);
                    if (!modalVariant.getShipping_quantity().equals("") && !modalVariant.getShipping_quantity().equals(AppURLParams.statusVal0)) {
                        selectedModalVariantList.add(modalVariant);
                    }
                }

                if (selectedModalVariantList.size() == 0) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_empty_modal_variant_quantity), Snackbar.LENGTH_LONG).show();
                    return;
                }
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_modal_variant_not_available), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (orderValue.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_order_value), Snackbar.LENGTH_SHORT).show();
                update_shipping_edittext_order_value.setError(getString(R.string.error_empty_order_value));
                update_shipping_edittext_order_value.requestFocus();
                return;
            }

            if (courier.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_courier), Snackbar.LENGTH_SHORT).show();
                update_shipping_edittext_courier.setError(getString(R.string.error_empty_courier));
                update_shipping_edittext_courier.requestFocus();
                return;
            }

            if (trackingId.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_tracking_id), Snackbar.LENGTH_SHORT).show();
                update_shipping_edittext_tracking_id.setError(getString(R.string.error_empty_tracking_id));
                update_shipping_edittext_tracking_id.requestFocus();
                return;
            }

            if (trackingLink.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_tracking_link), Snackbar.LENGTH_SHORT).show();
                update_shipping_edittext_tracking_link.setError(getString(R.string.error_empty_tracking_link));
                update_shipping_edittext_tracking_link.requestFocus();
                return;
            }

            if (address.equals("") && !updatingAddressImage) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_address), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (updatingAddressImage) {
                if (imageAddress != null) {
                    if (!imageAddress.exists()) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_attach_address_image), Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_address_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

//            if (secretNote.equals("")) {
//                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_tracking_link), Snackbar.LENGTH_SHORT).show();
//                update_shipping_edittext_secret_note.setError(getString(R.string.error_empty_tracking_link));
//                update_shipping_edittext_secret_note.requestFocus();
//                return;
//            }

            JSONArray modalVariantListJsonArray = new JSONArray();
            for (int i = 0; i < selectedModalVariantList.size(); i++) {

                ModalVariant modalVariant = selectedModalVariantList.get(i);
                String shippingQuantity = modalVariant.getShipping_quantity();
                Log.d(TAG, modalVariant.getVariant_name() + "\t" + shippingQuantity);

                if (shippingQuantity.equals("") && !shippingQuantity.equals(AppURLParams.statusVal0)) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_empty_cod_price), Snackbar.LENGTH_LONG).show();
                    return;
                }

                JSONObject variantJsonObject = new JSONObject();
                variantJsonObject.put(AppURLParams.requirement_variant_id, modalVariant.getRequirement_variant_id() + "");
                variantJsonObject.put(AppURLParams.variant_name, modalVariant.getVariant_name() + "");
                variantJsonObject.put(AppURLParams.variant_price, modalVariant.getVariant_price());
                variantJsonObject.put(AppURLParams.color, modalVariant.getSelected_color() + "");
                variantJsonObject.put(AppURLParams.quantity, modalVariant.getShipping_quantity() + "");
                variantJsonObject.put(AppURLParams.advance_pay, modalVariant.getAdvance_paid() + "");
                modalVariantListJsonArray.put(variantJsonObject);
            }
            Log.d(TAG, "modalVariantListJsonArray\n" + modalVariantListJsonArray);

            requestFor = 2;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserShippingUpdate());
            parameter.setParam(AppURLParams.requirement_id, userClaim.getRequirement_id() + "");
            parameter.setParam(AppURLParams.claim_confirm_id, selectedSiteData.getClaim_confirm_id() + "");
            parameter.setParam(AppURLParams.expected_date, expectedDate + "");
            parameter.setParam(AppURLParams.site_name, selectedSiteData.getSite_name() + "");
            parameter.setParam(AppURLParams.name_on_order, nameOnOrder + "");
            parameter.setParam(AppURLParams.model, userClaim.getModel_name() + "");
            parameter.setParam(AppURLParams.payment_mode, paymentMode + "");
            parameter.setParam(AppURLParams.order_value, orderValue);
            parameter.setParam(AppURLParams.courier, courier + "");
            parameter.setParam(AppURLParams.tracking_id, trackingId + "");
            parameter.setParam(AppURLParams.tracking_link, trackingLink + "");
            parameter.setParam(AppURLParams.address, address + "");
            parameter.setParam(AppURLParams.secret_note, secretNote + "");
            parameter.setParam(AppURLParams.variantList, modalVariantListJsonArray + "");

            if (updatingAddressImage) {
                if (imageAddress != null && imageAddress.exists()) {
                    String newPath = ImageProcessing.compressImage(this, imageAddress.toString());
                    parameter.setParam(AppURLParams.address_image, newPath);
                    Log.d(TAG, "Old Path =>" + imageAddress.toString() + "||New Path =>" + newPath);

                    new UploadFileHTTPAsync().execute(parameter);
                } else {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            } else {
                parameter.setParam(AppURLParams.address_image, "");
                callAPIViaPOST(parameter);
            }

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void callAPIViaGET(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri(), this, this) {
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
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void callAPIViaPOST(RequestParameter parameter) {
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

    private class UploadFileHTTPAsync extends AsyncTask<RequestParameter, Void, String> {

        private HttpClient mHttpClient;

        @Override
        protected String doInBackground(RequestParameter... reqParams) {

            try {
                RequestParameter parameter = reqParams[0];
                Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                mHttpClient = new DefaultHttpClient(params);
                HttpPost httppost = new HttpPost(parameter.getUri());

                final String token = ShPrefUserDetails.getToken(UserClaimUpdateShippingActivity.this);
                httppost.addHeader(AppURLParams.Authorization, token);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart(AppURLParams.requirement_id, new StringBody(parameter.getParam(AppURLParams.requirement_id)));
                multipartEntity.addPart(AppURLParams.claim_confirm_id, new StringBody(parameter.getParam(AppURLParams.claim_confirm_id)));
                multipartEntity.addPart(AppURLParams.expected_date, new StringBody(parameter.getParam(AppURLParams.expected_date)));
                multipartEntity.addPart(AppURLParams.site_name, new StringBody(parameter.getParam(AppURLParams.site_name)));
                multipartEntity.addPart(AppURLParams.name_on_order, new StringBody(parameter.getParam(AppURLParams.name_on_order)));
                multipartEntity.addPart(AppURLParams.model, new StringBody(parameter.getParam(AppURLParams.model)));

                multipartEntity.addPart(AppURLParams.payment_mode, new StringBody(parameter.getParam(AppURLParams.payment_mode)));
                multipartEntity.addPart(AppURLParams.order_value, new StringBody(parameter.getParam(AppURLParams.order_value)));
                multipartEntity.addPart(AppURLParams.courier, new StringBody(parameter.getParam(AppURLParams.courier)));
                multipartEntity.addPart(AppURLParams.tracking_id, new StringBody(parameter.getParam(AppURLParams.tracking_id)));
                multipartEntity.addPart(AppURLParams.tracking_link, new StringBody(parameter.getParam(AppURLParams.tracking_link)));
                multipartEntity.addPart(AppURLParams.address, new StringBody(parameter.getParam(AppURLParams.address)));
                multipartEntity.addPart(AppURLParams.secret_note, new StringBody(parameter.getParam(AppURLParams.secret_note)));
                multipartEntity.addPart(AppURLParams.variantList, new StringBody(parameter.getParam(AppURLParams.variantList)));

                if (!parameter.getParam(AppURLParams.address_image).equals("")) {
                    multipartEntity.addPart(AppURLParams.address_image, new FileBody(new File(parameter.getParam(AppURLParams.address_image))));
                    Log.d(TAG, AppURLParams.address_image + "  ----  " + parameter.getParam(AppURLParams.address_image));
                } else {
                    multipartEntity.addPart(AppURLParams.address_image, new StringBody(parameter.getParam(AppURLParams.address_image)));
                }

                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String responseString = EntityUtils.toString(r_entity);
                Log.d(TAG, "Image Uploading Server Response\t" + responseString);

                return responseString;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
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
                if (progress != null) {
                    progress.dismiss();
                }
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            switch (requestFor) {
                case 2:
                    shippingUpdated = true;
                    redirectUserProfile();
                    break;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (userClaim != null) {
                update_shipping_textview_event.setText(userClaim.getEvent_name());
                update_shipping_textview_required_quantity.setText(userClaim.getRequired_quantity() + "");
                update_shipping_textview_claim_quantity.setText(userClaim.getClaim_quantity() + "");
                update_shipping_textview_confirm_quantity.setText(userClaim.getConfirmed_quantity() + "");
                update_shipping_textview_dealer_name.setText(userClaim.getDealer_name() + "");
                update_shipping_textview_order_date.setText(AppUtils.convertDateWithoutTime(userClaim.getCreate_date()));
                update_shipping_textview_modal_name.setText(userClaim.getModel_name() + "");

                setDataModalVariantRecyclerAdapter();

                update_shipping_button_expected_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            final Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH);
                            int day = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(UserClaimUpdateShippingActivity.this, new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    try {
                                        DecimalFormat mFormat = new DecimalFormat("00");
                                        String s = year + "-" + mFormat.format(Double.valueOf((month + 1))) + "-" + mFormat.format(Double.valueOf(dayOfMonth));
                                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                        Date temp1 = f.parse(s);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.MILLISECOND, 0);
                                        calendar.set(Calendar.SECOND, 0);
                                        calendar.set(Calendar.MINUTE, 0);
                                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                                        if (temp1.getTime() < calendar.getTimeInMillis()) {
                                            Log.d("time", temp1.getTime() + ", " + calendar.getTimeInMillis());
                                            return;
                                        }

                                        expectedDate = AppUtils.convertDateWithoutTime(s);
                                        update_shipping_button_expected_date.setText(expectedDate);

                                    } catch (Exception e) {
                                        expectedDate = "";
                                        update_shipping_button_expected_date.setText(expectedDate);
                                        e.printStackTrace();
                                    }
                                }
                            }, year, month, day);
                            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);
                            datePickerDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureImage(View view) {
        try {
            int id = view.getId();
            String imageName = null, title = "";
            String s = Calendar.getInstance().getTimeInMillis() + "";
            mypath = null;

            switch (id) {
                case R.id.update_shipping_imageview_address_image:
                    captureImageForImageView = update_shipping_imageview_address_image;
                    captureImageForCloseImageView = update_shipping_imageview_address_image_close;
                    imageName = s + "_1.jpg";
                    imageAddress = new File(root, imageName);
                    mypath = imageAddress;
                    title = getString(R.string.address_image);
//                    clickedFor = 1;
                    break;
            }

            CropImage.startPickImageActivity(this);

        } catch (Exception e) {
            mypath = null;
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void clearImage(View view) {
        try {
            int id = view.getId();
            switch (id) {
                case R.id.update_shipping_imageview_address_image_close:
                    try {
                        if (imageAddress != null) {
                            AppUtils.removeImageFromStorage(this, imageAddress.toString());
                            imageAddress = null;
                            updatingAddressImage = true;
                        }
                        update_shipping_imageview_address_image.setImageResource(R.drawable.ic_arrow_up_gray);
                        update_shipping_imageview_address_image_close.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    private void setDataModalVariantRecyclerAdapter() {
        try {
            update_shipping_recyclerview_variants.invalidate();
            ArrayList<ModalVariant> modalVariantArrayList = selectedSiteData.getModalVariantArrayList();

            if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < modalVariantArrayList.size(); i++) {
                    ModalVariant modalVariant = modalVariantArrayList.get(i);
                    list.add(modalVariant.getVariant_name());
                }

                update_shipping_recyclerview_variants.setVisibility(View.VISIBLE);
                UpdateShippingModalVariantAdapter adapter = new UpdateShippingModalVariantAdapter(this, list, modalVariantArrayList);
                update_shipping_recyclerview_variants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                update_shipping_recyclerview_variants.setAdapter(adapter);
//                update_shipping_recyclerview_variants.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();

            } else {
                update_shipping_recyclerview_variants.setVisibility(View.GONE);
                giveErrorOnNoData();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            int id = adapterView.getId();
            if (id == R.id.update_shipping_spinner_payment_mode) {
                paymentMode = (update_shipping_spinner_payment_mode.getSelectedItemPosition() + 1) + "";
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void redirectUserProfile() {
        try {
            shippingUpdated = true;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri profileselectedUri = null;
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    captureImageForImageView.setImageURI(imageUri);
                    captureImageForCloseImageView.setVisibility(View.VISIBLE);
                    captureImageForCloseImageView.bringToFront();

                    profileselectedUri = imageUri;
                    imageAddress = new File(profileselectedUri.getPath());
                    updatingAddressImage = true;

                } else {
                    startCropImageActivity(imageUri);
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    captureImageForImageView.setImageURI(result.getUri());
                    captureImageForCloseImageView.setVisibility(View.VISIBLE);
                    captureImageForCloseImageView.bringToFront();

                    profileselectedUri = result.getUri();
                    imageAddress = new File(profileselectedUri.getPath());
                    updatingAddressImage = true;

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                }
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }

    private void giveErrorOnNoData() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (shippingUpdated) {
            setResult(81);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
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
}
