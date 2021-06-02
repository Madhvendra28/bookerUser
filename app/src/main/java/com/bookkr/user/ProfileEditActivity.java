package com.bookkr.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.States;
import com.model.UserProfile;
import com.preferences.ShPrefUserDetails;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.ImageProcessing;
import com.utils.JSONParser;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private CoordinatorLayout coordinatorLayout;
    private TextView profile_textview_dealer_account, profile_textview_dealer_since, profile_textview_status;

    private EditText profile_edittext_name, profile_edittext_mobile, profile_edittext_email, profile_edittext_address,
            profile_edittext_area, profile_edittext_pincode, profile_edittext_whatsapp_number,
            profile_edittext_facebook, profile_edittext_twitter, profile_edittext_instagram, profile_edittext_telegram, profile_edittext_doc_number;
    private static EditText profile_edittext_dob;

    private SearchableSpinner profile_spinner_state, profile_spinner_city;
    Spinner profile_spinner_doc_type;
    private RadioButton profile_radiobutton_male, profile_radiobutton_female;
    private CircleImageView profile_imageview_image1;
    private ImageView profile_imageview_image1_close, profile_imageview_warning, profile_imageview_doc_image,
            profile_imageview_doc_image_close, profile_imageview_calander;

    private ProgressDialog progress;
    private final String TAG = "User Profile Edit";
    private int requestFor = -1, clickedFor = 0;

    private File root;
    private File mypath;
    private File imageProfile, imageDoc;
    private ImageView captureImageForImageView, captureImageForCloseImageView;
    private boolean profileEdited = false, updatingProfileImage = false, updatingDocImage = false;

    private List<States> statesDistrictMap;
    private String state = null, city = null, docType = null, gender = null;
    private String statePin, districtPin;

    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            profile_textview_dealer_account = findViewById(R.id.profile_textview_dealer_account);
            profile_textview_dealer_since = findViewById(R.id.profile_textview_dealer_since);
            profile_textview_status = findViewById(R.id.profile_textview_status);

            profile_edittext_name = findViewById(R.id.profile_edittext_name);
            profile_edittext_mobile = findViewById(R.id.profile_edittext_mobile);
            profile_edittext_email = findViewById(R.id.profile_edittext_email);
            profile_edittext_address = findViewById(R.id.profile_edittext_address);
            profile_edittext_area = findViewById(R.id.profile_edittext_area);
            profile_edittext_pincode = findViewById(R.id.profile_edittext_pincode);

            profile_edittext_whatsapp_number = findViewById(R.id.profile_edittext_whatsapp_number);
            profile_edittext_dob = findViewById(R.id.profile_edittext_dob);
            profile_edittext_facebook = findViewById(R.id.profile_edittext_facebook);
            profile_edittext_twitter = findViewById(R.id.profile_edittext_twitter);
            profile_edittext_instagram = findViewById(R.id.profile_edittext_instagram);
            profile_edittext_telegram = findViewById(R.id.profile_edittext_telegram);
            profile_edittext_doc_number = findViewById(R.id.profile_edittext_doc_number);

            profile_spinner_state = findViewById(R.id.profile_spinner_state);
            profile_spinner_city = findViewById(R.id.profile_spinner_city);
            profile_spinner_doc_type = findViewById(R.id.profile_spinner_doc_type);

            profile_radiobutton_male = findViewById(R.id.profile_radiobutton_male);
            profile_radiobutton_female = findViewById(R.id.profile_radiobutton_female);

            profile_imageview_image1 = findViewById(R.id.profile_imageview_image1);
            profile_imageview_image1_close = findViewById(R.id.profile_imageview_image1_close);
            profile_imageview_warning = findViewById(R.id.profile_imageview_warning);
            profile_imageview_doc_image = findViewById(R.id.profile_imageview_doc_image);
            profile_imageview_doc_image_close = findViewById(R.id.profile_imageview_doc_image_close);
            profile_imageview_calander = findViewById(R.id.profile_imageview_calander);

            profile_spinner_state.setOnItemSelectedListener(this);
            profile_spinner_city.setOnItemSelectedListener(this);
            profile_spinner_doc_type.setOnItemSelectedListener(this);

            profile_radiobutton_male.setOnCheckedChangeListener(this);
            profile_radiobutton_female.setOnCheckedChangeListener(this);

            root = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (!root.exists()) {
                root.mkdirs();
            }

            userProfile = ProfileActivity.getProfileActivity().getUserProfile();
            if (statesDistrictMap == null) {
                getStatesDistrictsList();
            } else {
                if (userProfile != null) {
                    setDataInViews();
                } else {
                    giveErrorOnError();
                }
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
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
                case R.id.profile_imageview_image1:
                    captureImageForImageView = profile_imageview_image1;
                    captureImageForCloseImageView = profile_imageview_image1_close;
                    imageName = s + "_2.jpg";
                    imageProfile = new File(root, imageName);
                    mypath = imageProfile;
                    title = getString(R.string.profile_image);
                    clickedFor = 1;
                    break;

                case R.id.profile_imageview_doc_image:
                    captureImageForImageView = profile_imageview_doc_image;
                    captureImageForCloseImageView = profile_imageview_doc_image_close;
                    imageName = s + "_3.jpg";
                    imageDoc = new File(root, imageName);
                    mypath = imageDoc;
                    title = getString(R.string.document_image);
                    clickedFor = 2;
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
                case R.id.profile_imageview_image1_close:
                    try {
                        if (imageProfile != null) {
                            AppUtils.removeImageFromStorage(this, imageProfile.toString());
                            imageProfile = null;
                            updatingProfileImage = true;
                        }
                        profile_imageview_image1.setImageResource(R.drawable.avatar);
                        profile_imageview_image1_close.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.profile_imageview_doc_image_close:
                    try {
                        if (imageDoc != null) {
                            AppUtils.removeImageFromStorage(this, imageDoc.toString());
                            imageDoc = null;
                            updatingDocImage = true;
                        }
                        profile_imageview_doc_image.setImageResource(R.drawable.ic_arrow_up_gray);
                        profile_imageview_doc_image_close.setVisibility(View.GONE);
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

    public void selectDOB(View view) {
        AppUtils.hideKeyboard(this);
        DialogFragment newFragment = new DOBPickerFragment();
        newFragment.show(getSupportFragmentManager(), "DOBPickerFragment");
    }

    public static class DOBPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            dialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                DecimalFormat mFormat = new DecimalFormat("00");
                String s = year + "-" + mFormat.format(Double.valueOf((month + 1))) + "-" + mFormat.format(Double.valueOf(day));
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                profile_edittext_dob.setText(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getStatesDistrictsList() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            if (statesDistrictMap != null) {
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
            parameter.setUri(AppURL.getStateCityAPI());
            getDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void editProfile(View view) {
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
            profile_edittext_email.setError(null);
            profile_edittext_whatsapp_number.setError(null);
            profile_edittext_dob.setError(null);
            profile_edittext_address.setError(null);
            profile_edittext_area.setError(null);
            profile_edittext_pincode.setError(null);
            profile_edittext_doc_number.setError(null);

            String name = profile_edittext_name.getText() + "";
            String email = profile_edittext_email.getText().toString();
            String mobileNo = profile_edittext_mobile.getText() + "";
            String whatsappMobileNo = profile_edittext_whatsapp_number.getText() + "";
            String dob = profile_edittext_dob.getText() + "";
            String address = profile_edittext_address.getText().toString();
            String area = profile_edittext_area.getText().toString();
            String pincode = profile_edittext_pincode.getText().toString();
            String documentNo = profile_edittext_doc_number.getText().toString();

            String facebookLink = profile_edittext_facebook.getText().toString();
            String twitterLink = profile_edittext_twitter.getText().toString();
            String instaLink = profile_edittext_instagram.getText().toString();
            String telegramLink = profile_edittext_telegram.getText().toString();

            if (whatsappMobileNo.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_whatsapp_number), Snackbar.LENGTH_SHORT).show();
                profile_edittext_whatsapp_number.setError(getString(R.string.error_empty_whatsapp_number));
                profile_edittext_whatsapp_number.requestFocus();
                return;
            }

            if (gender == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_gender), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (dob.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_dob), Snackbar.LENGTH_SHORT).show();
                profile_edittext_dob.setError(getString(R.string.error_empty_dob));
                profile_edittext_dob.requestFocus();
                return;
            }

            if (!email.equals("")) {
                if (!email.contains("@") || !email.contains(".") || email.endsWith("@") || email.endsWith(".")) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_email), Snackbar.LENGTH_SHORT).show();
                    profile_edittext_email.setError(getString(R.string.error_invalid_email));
                    profile_edittext_email.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_email), Snackbar.LENGTH_SHORT).show();
                    profile_edittext_email.setError(getString(R.string.error_invalid_email));
                    profile_edittext_email.requestFocus();
                    return;
                }
            }

            if (address.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_address), Snackbar.LENGTH_SHORT).show();
                profile_edittext_address.setError(getString(R.string.error_empty_address));
                profile_edittext_address.requestFocus();
                return;
            }

            if (area.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_area), Snackbar.LENGTH_SHORT).show();
                profile_edittext_area.setError(getString(R.string.error_empty_area));
                profile_edittext_area.requestFocus();
                return;
            }

            if (pincode.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_pincode), Snackbar.LENGTH_SHORT).show();
                profile_edittext_pincode.setError(getString(R.string.error_empty_pincode));
                profile_edittext_pincode.requestFocus();
                return;
            }

            boolean b = AppUtils.isValidPinCode(pincode);
            if (!b) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_pincode), Snackbar.LENGTH_SHORT).show();
                profile_edittext_pincode.setError(getString(R.string.error_invalid_pincode));
                profile_edittext_pincode.requestFocus();
                return;
            }

            if (state == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_state), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (city == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_city), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (docType == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_document_type), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (documentNo.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_document_number), Snackbar.LENGTH_SHORT).show();
                profile_edittext_doc_number.setError(getString(R.string.error_empty_document_number));
                profile_edittext_doc_number.requestFocus();
                return;
            }

            if (updatingProfileImage) {
                if (imageProfile != null) {
                    if (!imageProfile.exists()) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

            if (updatingDocImage) {
                if (imageDoc != null) {
                    if (!imageDoc.exists()) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_attach_document_image), Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_document_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

            requestFor = 3;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_updating_profile));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getProfileEdit());
            parameter.setParam(AppURLParams.name, name + "");
            parameter.setParam(AppURLParams.contact_no, mobileNo + "");
            parameter.setParam(AppURLParams.whatsapp_no, whatsappMobileNo + "");
            parameter.setParam(AppURLParams.email_id, email + "");
            parameter.setParam(AppURLParams.gender, gender + "");
            parameter.setParam(AppURLParams.date_of_birth, dob + "");

            parameter.setParam(AppURLParams.address, address + "");
            parameter.setParam(AppURLParams.area, area + "");
            parameter.setParam(AppURLParams.postal_code, pincode + "");
            parameter.setParam(AppURLParams.state, state + "");
            parameter.setParam(AppURLParams.city, city + "");

            parameter.setParam(AppURLParams.facebook_link, facebookLink + "");
            parameter.setParam(AppURLParams.twitter_link, twitterLink + "");
            parameter.setParam(AppURLParams.instagram_link, instaLink + "");
            parameter.setParam(AppURLParams.telegram_link, telegramLink + "");

            parameter.setParam(AppURLParams.document_type, docType + "");
            parameter.setParam(AppURLParams.document_number, documentNo + "");

            parameter.setParam(AppURLParams.temp_profile_image, userProfile.getProfileImage() + "");
            parameter.setParam(AppURLParams.temp_document_image, userProfile.getDocumentImage() + "");

            if (updatingProfileImage) {
                if (imageProfile != null && imageProfile.exists()) {
                    String newPath = ImageProcessing.compressImage(this, imageProfile.toString());
                    parameter.setParam(AppURLParams.profile_image, newPath);
                    Log.d(TAG, "Old Path =>" + imageProfile.toString() + "||New Path =>" + newPath);

                } else {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                    return;
                }

            } else {
                parameter.setParam(AppURLParams.profile_image, "");
            }

            if (updatingDocImage) {
                if (imageDoc != null && imageDoc.exists()) {
                    String newPath = ImageProcessing.compressImage(this, imageDoc.toString());
                    parameter.setParam(AppURLParams.document_image, newPath);
                    Log.d(TAG, "Old Path =>" + imageDoc.toString() + "||New Path =>" + newPath);

                } else {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_document_image), Snackbar.LENGTH_LONG).show();
                    return;
                }

            } else {
                parameter.setParam(AppURLParams.document_image, "");
            }

            new UploadFileHTTPAsync().execute(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getDataListData(RequestParameter parameter) {
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

                final String token = ShPrefUserDetails.getToken(ProfileEditActivity.this);
                httppost.addHeader(AppURLParams.Authorization, token);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart(AppURLParams.name, new StringBody(parameter.getParam(AppURLParams.name)));
                multipartEntity.addPart(AppURLParams.contact_no, new StringBody(parameter.getParam(AppURLParams.contact_no)));
                multipartEntity.addPart(AppURLParams.whatsapp_no, new StringBody(parameter.getParam(AppURLParams.whatsapp_no)));
                multipartEntity.addPart(AppURLParams.email_id, new StringBody(parameter.getParam(AppURLParams.email_id)));
                multipartEntity.addPart(AppURLParams.gender, new StringBody(parameter.getParam(AppURLParams.gender)));
                multipartEntity.addPart(AppURLParams.date_of_birth, new StringBody(parameter.getParam(AppURLParams.date_of_birth)));

                multipartEntity.addPart(AppURLParams.address, new StringBody(parameter.getParam(AppURLParams.address)));
                multipartEntity.addPart(AppURLParams.area, new StringBody(parameter.getParam(AppURLParams.area)));
                multipartEntity.addPart(AppURLParams.state, new StringBody(parameter.getParam(AppURLParams.state)));
                multipartEntity.addPart(AppURLParams.city, new StringBody(parameter.getParam(AppURLParams.city)));
                multipartEntity.addPart(AppURLParams.postal_code, new StringBody(parameter.getParam(AppURLParams.postal_code)));

                multipartEntity.addPart(AppURLParams.facebook_link, new StringBody(parameter.getParam(AppURLParams.facebook_link)));
                multipartEntity.addPart(AppURLParams.twitter_link, new StringBody(parameter.getParam(AppURLParams.twitter_link)));
                multipartEntity.addPart(AppURLParams.instagram_link, new StringBody(parameter.getParam(AppURLParams.instagram_link)));
                multipartEntity.addPart(AppURLParams.telegram_link, new StringBody(parameter.getParam(AppURLParams.telegram_link)));

                multipartEntity.addPart(AppURLParams.document_type, new StringBody(parameter.getParam(AppURLParams.document_type)));
                multipartEntity.addPart(AppURLParams.document_number, new StringBody(parameter.getParam(AppURLParams.document_number)));

                multipartEntity.addPart(AppURLParams.temp_profile_image, new StringBody(parameter.getParam(AppURLParams.temp_profile_image)));
                multipartEntity.addPart(AppURLParams.temp_document_image, new StringBody(parameter.getParam(AppURLParams.temp_document_image)));

                if (!parameter.getParam(AppURLParams.profile_image).equals("")) {
                    multipartEntity.addPart(AppURLParams.profile_image, new FileBody(new File(parameter.getParam(AppURLParams.profile_image))));
                    Log.d(TAG, AppURLParams.profile_image + "  ----  " + parameter.getParam(AppURLParams.profile_image));
                } else {
                    multipartEntity.addPart(AppURLParams.profile_image, new StringBody(parameter.getParam(AppURLParams.profile_image)));
                }

                if (!parameter.getParam(AppURLParams.document_image).equals("")) {
                    multipartEntity.addPart(AppURLParams.document_image, new FileBody(new File(parameter.getParam(AppURLParams.document_image))));
                    Log.d(TAG, AppURLParams.document_image + "  ----  " + parameter.getParam(AppURLParams.document_image));
                } else {
                    multipartEntity.addPart(AppURLParams.document_image, new StringBody(parameter.getParam(AppURLParams.document_image)));
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
                    } else {
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
                case 1:
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(AppURLParams.data);
                        statesDistrictMap = JSONParser.parseStatesCityListModels(jsonArray);
                        if (statesDistrictMap != null && statesDistrictMap.size() > 0) {
                            setDataInSpinnerForStates();
                            setDataInViews();
                        } else {
                            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                    } catch (Exception e) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    }
                    break;

                case 2:
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(AppURLParams.data);
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        statePin = !jsonObject1.isNull(AppURLParams.state_name) ? jsonObject1.getString(AppURLParams.state_name) : null;
                        districtPin = !jsonObject1.isNull(AppURLParams.district) ? jsonObject1.getString(AppURLParams.district) : null;

                        if (statePin != null || districtPin != null) {
                            setDataInSpinnerForStatesForPincode();
                        } else {
                            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                    } catch (Exception e) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    }
                    break;

                case 3:
                    profileEdited = true;
                    redirectUserProfile();
                    break;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInSpinnerForStates() {
        try {
            if (statesDistrictMap != null && statesDistrictMap.size() > 0) {
                String[] stateArray = new String[statesDistrictMap.size()];
                for (int i = 0; i < statesDistrictMap.size(); i++) {
                    stateArray[i] = statesDistrictMap.get(i).getStateName();
                }
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateArray);
                profile_spinner_state.setAdapter(adapter);
                setDataInViews();
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInSpinnerForStatesForPincode() {
        try {
            Log.d(TAG, statePin + ", " + districtPin);
            if (statesDistrictMap != null && statesDistrictMap.size() > 0) {
                String[] stateArray = new String[statesDistrictMap.size()];
                for (int i = 0; i < statesDistrictMap.size(); i++) {
                    stateArray[i] = statesDistrictMap.get(i).getStateName();
                }
                //      Toast.makeText(this, "state : " + stateArray[0] + " selected : " + statePin, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < stateArray.length; i++) {
                    if (stateArray[i].toString().toLowerCase().equals(statePin.toLowerCase())) {
                        profile_spinner_state.setSelection(i);
                        ArrayList<String> cityList = statesDistrictMap.get(i).getCities();
                        Log.d(TAG, stateArray[i].toString().toUpperCase() + "\t" + districtPin + "\t" + cityList);
                        if (cityList.contains(districtPin)) {

                            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList);
                            profile_spinner_city.setAdapter(adapter);
                            Log.d(TAG, cityList.indexOf(districtPin) + "\t" + districtPin + "\t" + cityList);
                            profile_spinner_city.setSelection(cityList.indexOf(districtPin));
                        }
                    }
                }
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (userProfile != null) {
                profile_edittext_name.setText(userProfile.getName() + "");
                profile_edittext_mobile.setText(userProfile.getContact_no() + "");
                profile_edittext_whatsapp_number.setText(userProfile.getWhatsappNumber() + "");
                profile_edittext_email.setText(userProfile.getEmail_id() + "");
                profile_edittext_dob.setText(userProfile.getDate_of_birth() + "");
                profile_edittext_address.setText(userProfile.getAddress() + "");
                profile_edittext_area.setText(userProfile.getArea() + "");
                profile_edittext_pincode.setText(userProfile.getPostal_code() + "");

                profile_edittext_facebook.setText(userProfile.getFacebook_link() + "");
                profile_edittext_twitter.setText(userProfile.getTwitter_link() + "");
                profile_edittext_instagram.setText(userProfile.getInstagram_link() + "");
                profile_edittext_telegram.setText(userProfile.getTelegram_link() + "");

                if (userProfile.getStatus().equalsIgnoreCase(AppURLParams.statusVal1)) {
                    profile_textview_status.setText(getString(R.string.active));
                } else {
                    profile_textview_status.setText(getString(R.string.inactive));
                }

                if (userProfile.getVerification_status().equals(AppURLParams.statusVal1)) {
                    profile_textview_dealer_account.setText(getString(R.string.status_verified));
                    profile_imageview_warning.setImageResource(R.drawable.ic_action_check_sign);
                } else {
                    profile_textview_dealer_account.setText(getString(R.string.status_pending));
                    profile_imageview_warning.setImageResource(R.drawable.warning);
                }

                if (userProfile.getGender().equalsIgnoreCase(getString(R.string.gender_male))) {
                    profile_radiobutton_male.setChecked(true);
                } else if (userProfile.getGender().equalsIgnoreCase(getString(R.string.gender_female))) {
                    profile_radiobutton_female.setChecked(true);
                }

                String[] arr = getResources().getStringArray(R.array.document_type_items);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equalsIgnoreCase(userProfile.getDocument_type())) {
                        profile_spinner_doc_type.setSelection(i);
                        break;
                    }
                }
                profile_edittext_doc_number.setText(userProfile.getDocument_number() + "");
                statePin = userProfile.getState();
                districtPin = userProfile.getCity();

                if (statePin != null || districtPin != null) {
                    setDataInSpinnerForStatesForPincode();
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (userProfile.getProfileImageBitmap() != null) {
                    profile_imageview_image1.setImageBitmap(userProfile.getProfileImageBitmap());
                    profile_imageview_image1_close.setVisibility(View.VISIBLE);
                    profile_imageview_image1_close.bringToFront();
                } else {
                    profile_imageview_image1.setImageResource(R.drawable.avatar);
                    profile_imageview_image1_close.setVisibility(View.GONE);
                }

                if (userProfile.getDocumentImageBitmap() != null) {
                    profile_imageview_doc_image.setImageBitmap(userProfile.getDocumentImageBitmap());
                    profile_imageview_doc_image_close.setVisibility(View.VISIBLE);
                    profile_imageview_doc_image_close.bringToFront();
                } else {
                    profile_imageview_doc_image.setImageResource(R.drawable.ic_arrow_up_gray);
                    profile_imageview_doc_image_close.setVisibility(View.GONE);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            int id = adapterView.getId();
            if (id == R.id.profile_spinner_state) {
                state = (String) profile_spinner_state.getSelectedItem();
                int selectedStatePosition = profile_spinner_state.getSelectedItemPosition();
                ArrayList<String> strings = statesDistrictMap.get(selectedStatePosition).getCities();
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings);
                profile_spinner_city.setAdapter(adapter);
                state = (String) profile_spinner_state.getSelectedItem();
                profile_spinner_city.setSelection(strings.indexOf(userProfile.getCity()));

            } else if (id == R.id.profile_spinner_city) {
                city = (String) profile_spinner_city.getSelectedItem();

            } else if (id == R.id.profile_spinner_doc_type) {
                docType = (String) profile_spinner_doc_type.getSelectedItem();
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (isChecked) {
                int id = buttonView.getId();
                switch (id) {
                    case R.id.profile_radiobutton_male:
                        gender = getString(R.string.gender_male);
                        break;

                    case R.id.profile_radiobutton_female:
                        gender = getString(R.string.gender_female);
                        break;
                }
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void redirectUserProfile() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ack_account_edited);
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

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri profileselectedUri = null;
            Uri documentselectedUri = null;

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    captureImageForImageView.setImageURI(imageUri);
                    captureImageForCloseImageView.setVisibility(View.VISIBLE);
                    captureImageForCloseImageView.bringToFront();

                    if (clickedFor == 1) {
                        profileselectedUri = imageUri;
                        imageProfile = new File(profileselectedUri.getPath());
                        updatingProfileImage = true;
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

                    } else if (clickedFor == 2) {
                        documentselectedUri = imageUri;
                        imageDoc = new File(documentselectedUri.getPath());
                        updatingDocImage = true;
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    }
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

                    if (clickedFor == 1) {
                        profileselectedUri = result.getUri();
                        imageProfile = new File(profileselectedUri.getPath());
                        updatingProfileImage = true;
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

                    } else if (clickedFor == 2) {
                        documentselectedUri = result.getUri();
                        imageDoc = new File(documentselectedUri.getPath());
                        updatingDocImage = true;
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    }
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

    @Override
    public void onBackPressed() {
        if (profileEdited) {
            setResult(303);
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
