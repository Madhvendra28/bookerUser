package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
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
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ForgotPasswordActivity extends AppCompatActivity implements Response.Listener<String>,
        Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private LinearLayout forgot_password_LL_pin;
    private EditText forgot_password_textview_mobile_no, forgot_password_textview_pin;

    private ProgressDialog progress;
    private final String TAG = "Forgot Password";
    private String generatedOTP = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            forgot_password_LL_pin = findViewById(R.id.forgot_password_LL_pin);
            forgot_password_textview_mobile_no = findViewById(R.id.forgot_password_textview_mobile_no);
            forgot_password_textview_pin = findViewById(R.id.forgot_password_textview_pin);

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void verifyUser(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            forgot_password_LL_pin.setVisibility(View.GONE);
            String mobileNo = forgot_password_textview_mobile_no.getText() + "";
            if (mobileNo.equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile_no), Snackbar.LENGTH_SHORT).show();
                forgot_password_textview_mobile_no.setError(getString(R.string.error_empty_mobile_no));
                forgot_password_textview_mobile_no.requestFocus();
                return;
            }

            Random random = new Random();
            generatedOTP = String.format("%04d", random.nextInt(10000));

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_otp));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getForgetPassword());
            parameter.setParam(AppURLParams.contact_no, mobileNo + "");
            parameter.setParam(AppURLParams.otp, generatedOTP + "");
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

            Snackbar.make(coordinatorLayout, getString(R.string.otp_is_send_to_mobile_number), Snackbar.LENGTH_LONG).show();
            forgot_password_LL_pin.setVisibility(View.VISIBLE);
            forgot_password_textview_pin.requestFocus();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void verifyOTP(View view) {
        try {
            AppUtils.hideKeyboard(this);

            String mobileNo = forgot_password_textview_mobile_no.getText() + "";
            if (mobileNo.equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile_no), Snackbar.LENGTH_SHORT).show();
                forgot_password_textview_mobile_no.setError(getString(R.string.error_empty_mobile_no));
                forgot_password_textview_mobile_no.requestFocus();
                return;
            }

            String otp = forgot_password_textview_pin.getText() + "";
            if (otp.equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_otp), Snackbar.LENGTH_SHORT).show();
                forgot_password_textview_pin.setError(getString(R.string.error_empty_otp));
                forgot_password_textview_pin.requestFocus();
                return;
            }

            if (!otp.equals(generatedOTP)) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_otp_mismatch), Snackbar.LENGTH_SHORT).show();
                forgot_password_textview_pin.setError(getString(R.string.error_otp_mismatch));
                forgot_password_textview_pin.requestFocus();
                return;
            }

            resetUserPassword();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void resetUserPassword() {
        try {
            String mobileNo = forgot_password_textview_mobile_no.getText() + "";
            if (mobileNo.equalsIgnoreCase("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile_no), Snackbar.LENGTH_SHORT).show();
                forgot_password_textview_mobile_no.setError(getString(R.string.error_empty_mobile_no));
                forgot_password_textview_mobile_no.requestFocus();
                return;
            }

            Intent intent = new Intent(this, ForgotPasswordResetActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppURLParams.bundle_phone, mobileNo + "");
            intent.putExtra(AppURLParams.bundle, bundle);

            startActivityForResult(intent, 101);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 102) {
                onBackPressed();
            } else {
                forgot_password_textview_pin.setText("");
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
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
