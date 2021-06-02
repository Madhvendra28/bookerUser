package com.bookkr.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ForgotPasswordResetActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText change_password_edittext_new, change_password_edittext_retype_new;

    private ProgressDialog progress;
    private final String tag = "Change Password";

    private boolean passwordVisible = false, confirmPasswordVisible = false;
    private String mobileNo;
    private boolean passwordChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            change_password_edittext_new = findViewById(R.id.change_password_edittext_new);
            change_password_edittext_retype_new = findViewById(R.id.change_password_edittext_retype_new);

            mobileNo = getIntent().getBundleExtra(AppURLParams.bundle).getString(AppURLParams.bundle_phone);
            if (mobileNo == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void handlePasswordVisibility(View view) {
        try {
            if (passwordVisible) {
                passwordVisible = false;
                change_password_edittext_new.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_password_edittext_new.setSelection(change_password_edittext_new.getText().length());

            } else {
                passwordVisible = true;
                change_password_edittext_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                change_password_edittext_new.setSelection(change_password_edittext_new.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleConfirmPasswordVisibility(View view) {
        try {
            if (confirmPasswordVisible) {
                confirmPasswordVisible = false;
                change_password_edittext_retype_new.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_password_edittext_retype_new.setSelection(change_password_edittext_retype_new.getText().length());

            } else {
                confirmPasswordVisible = true;
                change_password_edittext_retype_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                change_password_edittext_retype_new.setSelection(change_password_edittext_retype_new.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            change_password_edittext_new.setError(null);
            change_password_edittext_retype_new.setError(null);

            if (mobileNo == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_user_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (mobileNo.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_user_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            String newPassword = change_password_edittext_new.getText().toString();
            if (newPassword.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_new_password), Snackbar.LENGTH_LONG).show();
                change_password_edittext_new.setError(getString(R.string.error_empty_new_password));
                change_password_edittext_new.requestFocus();
                return;
            }

            if (newPassword.length() < 4) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_min_length), Snackbar.LENGTH_SHORT).show();
                change_password_edittext_new.setError(getString(R.string.error_password_min_length));
                change_password_edittext_new.requestFocus();
                return;
            }

            String retyped = change_password_edittext_retype_new.getText().toString();
            if (retyped.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_retype_new_password), Snackbar.LENGTH_LONG).show();
                change_password_edittext_retype_new.setError(getString(R.string.error_empty_retype_new_password));
                change_password_edittext_retype_new.requestFocus();
                return;
            }

            if (!newPassword.equals(retyped)) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_confirm_no_match), Snackbar.LENGTH_LONG).show();
                change_password_edittext_retype_new.setError(getString(R.string.error_password_confirm_no_match));
                change_password_edittext_retype_new.requestFocus();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_change_password));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getForgetPassword());
            parameter.setParam(AppURLParams.phone, mobileNo);
            parameter.setParam(AppURLParams.password, newPassword);
            getDataFromServer(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getDataFromServer(RequestParameter parameter) {
        try {
            Log.d(tag, parameter.getUri() + "?" + parameter.getEncodedParams());
            final HashMap<String, String> params = parameter.getParams();
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(this, request);
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Log.d(tag, "server response => " + response);

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

    private void processResponse(JSONObject jsonObject) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String ack = getString(R.string.dialog_change_password);
            String button = "Login";
            builder.setMessage(ack);
            builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    passwordChanged = true;
                    onBackPressed();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } catch (Exception e) {
            if (progress != null)
                progress.dismiss();
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (passwordChanged) {
            setResult(102);
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
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
