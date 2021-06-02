package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fcm.NotificationUtils;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.model.RequestParameter;
import com.model.UserProfile;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class UserSignUpOTPVerificationActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText otp_edittext_otp;
    private TextView otp_textview_mobile_no;

    private int requestFor = -1;//1 = resend otp , 2 register user
    private ProgressDialog progress;
    private final String TAG = "OTP Verification";

    private UserProfile userProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_otpverification);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            otp_edittext_otp = findViewById(R.id.otp_edittext_otp);
            otp_textview_mobile_no = findViewById(R.id.otp_textview_mobile_no);

            userProfile = getIntent().getExtras().getParcelable(AppURLParams.bundle_user_details);
            if (userProfile == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            otp_textview_mobile_no.setText(userProfile.getContact_no() + "");

            Task<Void> task = SmsRetriever.getClient(this).startSmsUserConsent(null /* or null */);
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            registerReceiver(smsVerificationReceiver, intentFilter);

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, message + "");
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.

                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    otp_edittext_otp.setText(oneTimeCode + "");
                    otp_edittext_otp.setSelection(otp_edittext_otp.getText().length());
                    checkOTP(null);
                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;

        }
    }

    private String parseOneTimeCode(String s) {
        s = s.substring(s.lastIndexOf("#") + 1, s.length() - 1);
        Log.d(TAG, "OTP is " + s);
        return s;
    }

    public void resendOTP(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            if (userProfile == null || userProfile.getContact_no() == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_otp_later), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (userProfile.getContact_no().equals("")) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_otp_later), Snackbar.LENGTH_SHORT).show();
                return;
            }

            Random random = new Random();
            String generatedOtp = String.format("%04d", random.nextInt(10000));
            Log.d(TAG, "Generated OTP : " + generatedOtp);

            requestFor = 1;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_otp));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            userProfile.setOtp(generatedOtp);

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getCheckUser());
            parameter.setParam(AppURLParams.contact_no, userProfile.getContact_no() + "");
            parameter.setParam(AppURLParams.otp, generatedOtp);
            parameter.setParam(AppURLParams.email_id, userProfile.getEmail_id() + "");
            sendDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void checkOTP(View view) {
        try {
            AppUtils.hideKeyboard(this);
            otp_edittext_otp.setError(null);

            String entereedOTP = otp_edittext_otp.getText().toString();
            if (entereedOTP.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_otp), Snackbar.LENGTH_SHORT).show();
                otp_edittext_otp.setError(getString(R.string.error_empty_otp));
                otp_edittext_otp.requestFocus();
                return;
            }

            if (userProfile == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (entereedOTP.equals(userProfile.getOtp())) {
//                verification done now save user profile
//                registerUser();

                String s = ShPrefUserDetails.getFCMRegId(this);
                if (s == null) {
                    enableFCM();
                } else {
                    registerUser(s);
                }
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_otp_mismatch), Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void registerUser(String deviceId) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            if (deviceId == null) {
                deviceId = "Not Available!";
            }
            Log.d(TAG, "in prepareData " + ShPrefUserDetails.getFCMRegId(this) + "");

            if (userProfile.getName().equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_name), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (userProfile.getContact_no().equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (userProfile.getContact_no().length() < 10) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_mobile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            requestFor = 2;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_create_user));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getRegistration());
            parameter.setParam(AppURLParams.name, userProfile.getName());
            parameter.setParam(AppURLParams.email_id, userProfile.getEmail_id());
            parameter.setParam(AppURLParams.contact_no, userProfile.getContact_no());
            parameter.setParam(AppURLParams.whatsapp_no, userProfile.getWhatsappNumber() + "");
//            parameter.setParam(AppURLParams.gender, userProfile.getGender());
            parameter.setParam(AppURLParams.refer_code, userProfile.getReferralCode() + "");
            parameter.setParam(AppURLParams.password, userProfile.getPassword());
            parameter.setParam(AppURLParams.captcha, userProfile.getCaptcha()+"");
            parameter.setParam(AppURLParams.deviceId, deviceId);
            sendDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendDataListData(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
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
                } else { // if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
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
            if (progress != null) {
                progress.dismiss();
            }

            switch (requestFor) {
                case 1:
                    Snackbar.make(coordinatorLayout, getString(R.string.ack_otp_sent), Snackbar.LENGTH_SHORT).show();
                    break;

                case 2:
                    try {
                        JSONObject jsonObject1 = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONObject(AppURLParams.data) : null;
                        if (jsonObject1 != null) {
                            UserProfile userProfile = JSONParser.parseUserProfile(jsonObject1);

                            if (userProfile != null) {
                                boolean b = ShPrefUserDetails.storeUserProfile(this, userProfile);
                                if (b) {
                                    this.userProfile = userProfile;
                                    redirectUserToHome();
                                } else {
                                    Snackbar.make(coordinatorLayout, getString(R.string.error_save_profile), Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectUserToHome() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ack_account_created);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    redirectUser();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void redirectUser() {
        try {
            String s = ShPrefUserDetails.getToken(this);
            if (s != null) {
                nowProceedToHome();
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void nowProceedToHome() {
        try {
            setResult(201);
            finish();
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(101);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsVerificationReceiver);
    }

    //       ----------------- FCM ---------------------------------------------------------------------

    private void enableFCM() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        displayFirebaseRegId();
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.d(TAG, "save FCM Token addOnSuccessListener = " + token);
                    ShPrefUserDetails.setFCMRegId(UserSignUpOTPVerificationActivity.this, token);
                    displayFirebaseRegId();
                }

               /* @Override
                public void onComplete(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    Log.d(TAG, "save FCM Token addOnSuccessListener = "+ token);
                    ShPrefUserDetails.setFCMRegId(LoginActivity.this, token);
                    displayFirebaseRegId();
                }*/
            });

            NotificationUtils.clearNotifications(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayFirebaseRegId() {
        String regId = ShPrefUserDetails.getFCMRegId(this);
        Log.d("Firebase", "Firebase reg id: " + regId);

        if (regId != null) {
            if (!regId.equals("")) {
                registerUser(regId);
            } else {
                registerUser(null);
            }
        } else {
            registerUser(null);
        }
    }
}
