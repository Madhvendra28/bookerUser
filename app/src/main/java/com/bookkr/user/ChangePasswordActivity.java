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
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ChangePasswordActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText change_password_edittext_current, change_password_edittext_new, change_password_edittext_retype_new;

    private ProgressDialog progress;
    private final String tag = "Change Password";
    private boolean passwordVisible = false, newPasswordVisible = false, confirmPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        try {
//            Toolbar toolbar = findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            change_password_edittext_current = findViewById(R.id.change_password_edittext_current);
            change_password_edittext_new = findViewById(R.id.change_password_edittext_new);
            change_password_edittext_retype_new = findViewById(R.id.change_password_edittext_retype_new);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void handlePasswordVisibility(View view) {
        try {
            if (passwordVisible) {
                passwordVisible = false;
                change_password_edittext_current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_password_edittext_current.setSelection(change_password_edittext_current.getText().length());

            } else {
                passwordVisible = true;
                change_password_edittext_current.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                change_password_edittext_current.setSelection(change_password_edittext_current.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleNewPasswordVisibility(View view) {
        try {
            if (newPasswordVisible) {
                newPasswordVisible = false;
                change_password_edittext_new.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                change_password_edittext_new.setSelection(change_password_edittext_new.getText().length());

            } else {
                newPasswordVisible = true;
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
            change_password_edittext_current.setError(null);
            change_password_edittext_new.setError(null);
            change_password_edittext_retype_new.setError(null);

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            String current = change_password_edittext_current.getText().toString();
            if (current.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_current_password), Snackbar.LENGTH_LONG).show();
                change_password_edittext_current.setError(getString(R.string.error_empty_current_password));
                change_password_edittext_current.requestFocus();
                return;
            }

            if (current.length() < 4) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_min_length), Snackbar.LENGTH_SHORT).show();
                change_password_edittext_current.setError(getString(R.string.error_password_min_length));
                change_password_edittext_current.requestFocus();
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
            parameter.setUri(AppURL.getAppURL() + AppURL.getChangePassword());
            parameter.setParam(AppURLParams.user_id, userId);
            parameter.setParam(AppURLParams.current_password, current);
            parameter.setParam(AppURLParams.new_password, newPassword);
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
            String button = "Thank You";
            builder.setMessage(ack);
            builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
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
