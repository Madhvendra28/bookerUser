package com.bookkr.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.UserClaim;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserClaimFailedActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText user_claim_edittext_quantity, user_claim_edittext_reason;

    private ProgressDialog progress;
    private final String TAG = "User Profile Edit";
    private int requestFor = -1, clickedFor = 0;
    private boolean bookingFailed = false;

    private UserClaim userClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_failed);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            user_claim_edittext_quantity = findViewById(R.id.user_claim_edittext_quantity);
            user_claim_edittext_reason = findViewById(R.id.user_claim_edittext_reason);

            userClaim = UserClaimHistoryListActivity.getActivity().getSelectedUserClaim();
            if (userClaim == null) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                return;
            } else {
                setDataInViews();
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (userClaim != null) {
                user_claim_edittext_quantity.setText(userClaim.getFailed_quantity() + "");
                user_claim_edittext_reason.setText(userClaim.getFail_quantity_reason() + "");

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_user_profile), Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void saveFailedData(View view) {
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
            user_claim_edittext_quantity.setError(null);
            user_claim_edittext_reason.setError(null);

            String quantity = user_claim_edittext_quantity.getText() + "";
            String reason = user_claim_edittext_reason.getText().toString();

            if (quantity.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_quantity), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_quantity.setError(getString(R.string.error_empty_quantity));
                user_claim_edittext_quantity.requestFocus();
                return;
            }

            Integer claimQuantityInt = Integer.parseInt(userClaim.getQuantity());
            Integer failedQuantityInt = Integer.parseInt(quantity);
            if (failedQuantityInt > claimQuantityInt) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_claim_failed_quantity_mismatch), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_quantity.setError(getString(R.string.error_claim_failed_quantity_mismatch));
                user_claim_edittext_quantity.requestFocus();
                return;
            }

            if (reason.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_reason), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_reason.setError(getString(R.string.error_empty_reason));
                user_claim_edittext_reason.requestFocus();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getBookingFail());
            parameter.setParam(AppURLParams.claim_requirement_id, userClaim.getClaim_requirement_id() + "");
            parameter.setParam(AppURLParams.fail_quantity, quantity + "");
            parameter.setParam(AppURLParams.fail_quantity_reason, reason + "");
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

    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            bookingFailed = true;
            redirectClaimList();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void redirectClaimList() {
        try {
            bookingFailed = true;
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
    public void onBackPressed() {
        if (bookingFailed) {
            setResult(63);
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

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }
}
