package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class UserSignUpActivityPart2 extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText signup_edittext_password, signup_edittext_confirm_password, signup_edittext_referral_code, signup_edittext_captcha;
    private TextView signup_textview_password_referral_response;
    private CheckBox signup_checkbox_tnc;

    private int requestFor = -1;
    private final String TAG = "User Signup 2";
    private ProgressDialog progress;

    private boolean passwordVisible = false, confirmPasswordVisible = false, isValidReferralCode = false;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_part2);

        try {
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            signup_edittext_password = findViewById(R.id.signup_edittext_password);
            signup_edittext_confirm_password = findViewById(R.id.signup_edittext_confirm_password);
            signup_edittext_referral_code = findViewById(R.id.signup_edittext_referral_code);

            signup_textview_password_referral_response = findViewById(R.id.signup_textview_password_referral_response);
            signup_edittext_captcha = findViewById(R.id.signup_edittext_captcha);
            signup_checkbox_tnc = findViewById(R.id.signup_checkbox_tnc);

            userProfile = getIntent().getExtras().getParcelable(AppURLParams.bundle_user_details);
            if (userProfile == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void handlePasswordVisibility(View view) {
        try {
            if (passwordVisible) {
                passwordVisible = false;
                signup_edittext_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                signup_edittext_password.setSelection(signup_edittext_password.getText().length());

            } else {
                passwordVisible = true;
                signup_edittext_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                signup_edittext_password.setSelection(signup_edittext_password.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleConfirmPasswordVisibility(View view) {
        try {
            if (confirmPasswordVisible) {
                confirmPasswordVisible = false;
                signup_edittext_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                signup_edittext_confirm_password.setSelection(signup_edittext_confirm_password.getText().length());

            } else {
                confirmPasswordVisible = true;
                signup_edittext_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                signup_edittext_confirm_password.setSelection(signup_edittext_confirm_password.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkReferralCode(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            isValidReferralCode = false;

            signup_edittext_referral_code.setError(null);
            String referralCode = signup_edittext_referral_code.getText().toString();

            if (userProfile == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (referralCode.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_referral_code), Snackbar.LENGTH_SHORT).show();
                signup_edittext_referral_code.setError(getString(R.string.error_empty_referral_code));
                signup_edittext_referral_code.requestFocus();
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
            parameter.setUri(AppURL.getAppURL() + AppURL.getCheckReferral());
            parameter.setParam(AppURLParams.refer_code, referralCode + "");
            sendDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
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
            signup_edittext_password.setError(null);
            signup_edittext_confirm_password.setError(null);
            signup_edittext_referral_code.setError(null);
            signup_edittext_captcha.setError(null);

            String password = signup_edittext_password.getText().toString();
            String confirmPassword = signup_edittext_confirm_password.getText().toString();
            String referralCode = signup_edittext_referral_code.getText().toString();
            String captcha = signup_edittext_captcha.getText() + "";
            boolean tncAccepted = signup_checkbox_tnc.isChecked();

            if (userProfile == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_user_profile), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (password.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_password), Snackbar.LENGTH_SHORT).show();
                signup_edittext_password.setError(getString(R.string.error_empty_password));
                signup_edittext_password.requestFocus();
                return;
            }

            if (password.length() < 4) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_min_length), Snackbar.LENGTH_SHORT).show();
                signup_edittext_password.setError(getString(R.string.error_password_min_length));
                signup_edittext_password.requestFocus();
                return;
            }

            if (confirmPassword.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_confirm_password), Snackbar.LENGTH_SHORT).show();
                signup_edittext_confirm_password.setError(getString(R.string.error_empty_confirm_password));
                signup_edittext_confirm_password.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_confirm_no_match), Snackbar.LENGTH_SHORT).show();
                signup_edittext_confirm_password.setError(getString(R.string.error_password_confirm_no_match));
                signup_edittext_confirm_password.requestFocus();
                return;
            }

            if (!referralCode.equals("")) {
                if(!isValidReferralCode){
                    Snackbar.make(coordinatorLayout, getString(R.string.error_invalid_referral_code), Snackbar.LENGTH_SHORT).show();
                    signup_edittext_referral_code.setError(getString(R.string.error_invalid_referral_code));
                    signup_edittext_referral_code.requestFocus();
                    return;

                }
            }

            if (captcha.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_captcha), Snackbar.LENGTH_SHORT).show();
                signup_edittext_captcha.setError(getString(R.string.error_empty_captcha));
                signup_edittext_captcha.requestFocus();
                return;
            }

            if(!tncAccepted){
                Snackbar.make(coordinatorLayout, getString(R.string.error_accept_tnc), Snackbar.LENGTH_SHORT).show();
                return;
            }

            Random random = new Random();
            String generatedOtp = String.format("%04d", random.nextInt(10000));
            Log.d(TAG, "Generated OTP : " + generatedOtp);

            requestFor = 2;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_otp));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            userProfile.setReferralCode(referralCode);
            userProfile.setPassword(password);
            userProfile.setOtp(generatedOtp);
            userProfile.setCaptcha(captcha);

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getCheckUser());
            parameter.setParam(AppURLParams.contact_no, userProfile.getContact_no() + "");
            parameter.setParam(AppURLParams.otp, generatedOtp);
            parameter.setParam(AppURLParams.email_id, userProfile.getEmail_id());
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
                } else {
                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                    if (requestFor == 1) {
                        signup_edittext_referral_code.setText("");
                    }
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
                    try {
                        String s = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getString(AppURLParams.data) : null;
                        if (s != null) {
                            signup_textview_password_referral_response.setText(s + "");
                            signup_textview_password_referral_response.setVisibility(View.VISIBLE);
                            isValidReferralCode = true;

                        } else {
                            signup_textview_password_referral_response.setText("");
                            signup_textview_password_referral_response.setVisibility(View.GONE);
                            isValidReferralCode = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 101) {
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
