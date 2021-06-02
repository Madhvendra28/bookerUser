package com.bookkr.user;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.UserProfile;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class UserSignUpActivity extends Activity implements Response.Listener<String>, Response.ErrorListener,
        CompoundButton.OnCheckedChangeListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText signup_edittext_name, signup_edittext_mobile, signup_edittext_email, signup_edittext_whatsapp_number;
    private RadioButton signup_radiobutton_same_mobile, signup_radiobutton_other_mobile;

    private final String TAG = "User Signup";
    private ProgressDialog progress;
    private int requestFor = -1; // 1 = get state city, 2= send otp

    private String whatsappNumberType = ""; //, gender = "";
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        try {
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            signup_edittext_name = findViewById(R.id.signup_edittext_name);
            signup_edittext_mobile = findViewById(R.id.signup_edittext_mobile);
            signup_edittext_email = findViewById(R.id.signup_edittext_email);
            signup_edittext_whatsapp_number = findViewById(R.id.signup_edittext_whatsapp_number);
            signup_radiobutton_same_mobile = findViewById(R.id.signup_radiobutton_same_mobile);
            signup_radiobutton_other_mobile = findViewById(R.id.signup_radiobutton_other_mobile);

            signup_radiobutton_same_mobile.setOnCheckedChangeListener(this);
            signup_radiobutton_other_mobile.setOnCheckedChangeListener(this);

            requestHint();
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (isChecked) {
                int id = buttonView.getId();
                switch (id) {
                    case R.id.signup_radiobutton_same_mobile:
                        whatsappNumberType = getString(R.string.same_number);
                        String mobileNo = signup_edittext_mobile.getText() + "";
                        signup_edittext_whatsapp_number.setText(mobileNo);
                        signup_edittext_whatsapp_number.setSelection(signup_edittext_whatsapp_number.getText().length());
                        break;

                    case R.id.signup_radiobutton_other_mobile:
                        whatsappNumberType = getString(R.string.other_number);
                        signup_edittext_whatsapp_number.setText("");
                        break;

                }
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void userSignUp(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            userProfile = null;
            signup_edittext_name.setError(null);
            signup_edittext_mobile.setError(null);
            signup_edittext_email.setError(null);
            signup_edittext_whatsapp_number.setError(null);

            String name = signup_edittext_name.getText() + "";
            String mobileNo = signup_edittext_mobile.getText() + "";
            String email = signup_edittext_email.getText().toString();
            String whatsappNumber = signup_edittext_whatsapp_number.getText().toString();

            if (name.equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_name), Snackbar.LENGTH_SHORT).show();
                signup_edittext_name.setError(getString(R.string.error_empty_name));
                signup_edittext_name.requestFocus();
                return;
            }

            if (mobileNo.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile), Snackbar.LENGTH_SHORT).show();
                signup_edittext_mobile.setError(getString(R.string.error_empty_mobile));
                signup_edittext_mobile.requestFocus();
                return;
            }

            if (mobileNo.length() < 10) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_mobile), Snackbar.LENGTH_SHORT).show();
                signup_edittext_mobile.setError(getString(R.string.error_invalid_mobile));
                signup_edittext_mobile.requestFocus();
                return;
            }

            /*if (email.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_email), Snackbar.LENGTH_SHORT).show();
                signup_edittext_mobile.setError(getString(R.string.error_empty_email));
                signup_edittext_mobile.requestFocus();
                return;
            }*/

            if (!email.equals("")) {
                if (!email.contains("@") || !email.contains(".") || email.endsWith("@") || email.endsWith(".")) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_email), Snackbar.LENGTH_SHORT).show();
                    signup_edittext_email.setError(getString(R.string.error_invalid_email));
                    signup_edittext_email.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_email), Snackbar.LENGTH_SHORT).show();
                    signup_edittext_email.setError(getString(R.string.error_invalid_email));
                    signup_edittext_email.requestFocus();
                    return;
                }
            }

            if (whatsappNumberType == null || whatsappNumberType.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_whatsapp_number), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (whatsappNumberType.equals(getString(R.string.other_number))) {
                if (whatsappNumber.equals("")) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_empty_whatsapp_number), Snackbar.LENGTH_SHORT).show();
                    signup_edittext_whatsapp_number.setError(getString(R.string.error_empty_whatsapp_number));
                    signup_edittext_whatsapp_number.requestFocus();
                    return;
                }
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

            userProfile = new UserProfile();
            userProfile.setName(name);
            userProfile.setContact_no(mobileNo);
            userProfile.setEmail_id(email);
            userProfile.setWhatsappNumber(whatsappNumber);
            userProfile.setOtp(generatedOtp);

            sendToRegistrationDetailsPart2();

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
                    verifyMobileNoOTP();
                    break;
            }


        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void verifyMobileNoOTP() {
        try {
            if (userProfile != null) {
                Intent intent = new Intent(this, UserSignUpOTPVerificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppURLParams.bundle_user_details, userProfile);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendToRegistrationDetailsPart2() {
        try {
            if (progress != null) {
                progress.dismiss();
            }

            if (userProfile != null) {
                Intent intent = new Intent(this, UserSignUpActivityPart2.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppURLParams.bundle_user_details, userProfile);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static final int CREDENTIAL_PICKER_REQUEST = 1;  // Set to an unused request code

    // Construct a request for contact_no numbers and show the picker
    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CREDENTIAL_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    // Obtain the contact_no number from the result
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process contact_no number string
                    Log.d(TAG, credential + "\n" + credential.getId());
                    String mobileNo = credential.getId() + "";
                    if (!mobileNo.equals(null) || !mobileNo.equals("")) {
                        mobileNo = mobileNo.replace("+91", "");
                        Log.d(TAG, mobileNo + "");
                        signup_edittext_mobile.setText(mobileNo);
                    }
                }

            } else if (resultCode == 101) {
//                back from otp verification screen, not registered
//                do nothing

            } else if (resultCode == 201) {
//                back from reg screen, registered
                setResult(201);
                finish();
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }

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
}
