package com.bookkr.user;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fcm.NotificationUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class LoginActivity extends Activity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private EditText login_textview_login_id, login_edittext_password;

    private boolean passwordVisible = false;
    private ProgressDialog progress;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            login_textview_login_id = findViewById(R.id.login_textview_login_id);
            login_edittext_password = findViewById(R.id.login_edittext_password);

            String s = ShPrefUserDetails.getToken(this);
            if (s != null) {
                nowProceedToHome();
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                checkPermissions();
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void handlePasswordVisibility(View view) {
        try {
            login_edittext_password = findViewById(R.id.login_edittext_password);

            if (passwordVisible) {
                passwordVisible = false;
                login_edittext_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                login_edittext_password.setSelection(login_edittext_password.getText().length());

            } else {
                passwordVisible = true;
                login_edittext_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                login_edittext_password.setSelection(login_edittext_password.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userLogin(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            AppUtils.hideKeyboard(this);
            login_textview_login_id.setError(null);
            login_edittext_password.setError(null);

            String loginId = login_textview_login_id.getText().toString();
            String password = login_edittext_password.getText().toString();

            if (loginId.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile_no), Snackbar.LENGTH_SHORT).show();
                login_textview_login_id.setError(getString(R.string.error_empty_mobile_no));
                login_textview_login_id.requestFocus();
                return;
            }

            if (password.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_required), Snackbar.LENGTH_SHORT).show();
                login_edittext_password.setError(getString(R.string.error_password_required));
                login_edittext_password.requestFocus();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_verifying_user));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            String s = ShPrefUserDetails.getFCMRegId(this);
            if (s == null) {
                enableFCM();
            } else {
                prepareData(s);
            }
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void prepareData(String deviceId) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                if (progress != null)
                    progress.dismiss();
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            if (deviceId == null) {
                deviceId = "Not Available!";
            }

            Log.d(TAG, "in prepareData " + ShPrefUserDetails.getFCMRegId(this) + "");
            String loginId = login_textview_login_id.getText().toString();
            String password = login_edittext_password.getText().toString();

            if (loginId.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_mobile_no), Snackbar.LENGTH_SHORT).show();
                login_textview_login_id.setError(getString(R.string.error_empty_mobile_no));
                login_textview_login_id.requestFocus();
                return;
            }

            if (password.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_password_required), Snackbar.LENGTH_SHORT).show();
                login_edittext_password.setError(getString(R.string.error_password_required));
                login_edittext_password.requestFocus();
                return;
            }

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getLogin());
            parameter.setParam(AppURLParams.contact_no, loginId);
            parameter.setParam(AppURLParams.password, password);
            parameter.setParam(AppURLParams.deviceId, deviceId);
            sendUserDetailsToVerify(parameter);

        } catch (Exception e) {
            if (progress != null)
                progress.dismiss();
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendUserDetailsToVerify(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final HashMap<String, String> params = parameter.getParams();
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
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
                } else{
                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        try {
            if (progress != null) {
                progress.dismiss();
            }

            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
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

            JSONObject jsonObject1 = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONObject(AppURLParams.data) : null;
            if (jsonObject1 != null) {
                UserProfile userProfile = JSONParser.parseUserProfile(jsonObject1);

                if (userProfile != null) {
                    boolean b = ShPrefUserDetails.storeUserProfile(this, userProfile);
                    if (b) {
                        redirectUser();
                    } else {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_save_profile), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                    return;
                }

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(this, HomeActivity.class);
            startActivityForResult(intent, 200);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void userSignUp(View view) {
        try {
            Intent intent = new Intent(this, UserSignUpActivity.class);
            startActivityForResult(intent, 100);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void forgotPassword(View view) {
        try {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
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
//            merchantIdentifier = null;

            if (resultCode == 200) {
                login_textview_login_id.setText("");
                login_edittext_password.setText("");

            } else if (resultCode == 101) {
//                do nothinh

            } else if (resultCode == 201) {
                login_textview_login_id.setText("");
                login_edittext_password.setText("");
                redirectUser();

            } else {
                finish();
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
                    ShPrefUserDetails.setFCMRegId(LoginActivity.this, token);
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
                prepareData(regId);
            } else {
                prepareData(null);
            }
        } else {
            prepareData(null);
        }
    }

//       ----------------- 6.0 ----------------------------
//        permission for 6.0 or higher

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public List<String> permissionsNeeded;
    public List<String> permissionsList;

    @TargetApi(23)
    private void checkPermissions() {
        permissionsNeeded = new ArrayList<String>();
        permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.INTERNET))
            permissionsNeeded.add("INTERNET");
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("ACCESS NETWORK STATE");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                try {
                    Map<String, Integer> perms = new HashMap<String, Integer>();
                    // Initial
                    perms.put(android.Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                    perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                    perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                    // Fill with results
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for ACCESS_FINE_LOCATION
                    if (perms.get(android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // All Permissions Granted

                    } else {
                        // Permission Denied
                        Snackbar.make(coordinatorLayout, "Some Permission is Denied", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
