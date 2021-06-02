package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.UserProfile;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private CoordinatorLayout coordinatorLayout;
    private CircleImageView profile_imageview_image;
    private TextView profile_textview_name, profile_textview_phone, profile_textview_email, profile_textview_address,
            profile_textview_area, profile_textview_state, profile_textview_city, profile_textview_status,
            profile_textview_pincode, profile_textview_dealer_account, profile_textview_dealer_since, profile_textview_whatsapp_number,
            profile_textview_gender, profile_textview_dob, profile_textview_facebook, profile_textview_twitter, profile_textview_instagram,
            profile_textview_telegram, profile_textview_doc_type, profile_textview_doc_number;
    private ImageView profile_imageview_warning, profile_imageview_doc_image;

    private ProgressDialog progress;
    private final String TAG = "User Profile";

    private static ProfileActivity profileActivity;
    private UserProfile userProfile;
    private boolean profileUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            profile_imageview_image = findViewById(R.id.profile_imageview_image);
            profile_textview_name = findViewById(R.id.profile_textview_name);
            profile_textview_phone = findViewById(R.id.profile_textview_phone);
            profile_textview_email = findViewById(R.id.profile_textview_email);
            profile_textview_address = findViewById(R.id.profile_textview_address);
            profile_textview_area = findViewById(R.id.profile_textview_area);
            profile_textview_state = findViewById(R.id.profile_textview_state);
            profile_textview_city = findViewById(R.id.profile_textview_city);
            profile_textview_pincode = findViewById(R.id.profile_textview_pincode);
            profile_textview_status = findViewById(R.id.profile_textview_status);

            profile_textview_dealer_account = findViewById(R.id.profile_textview_dealer_account);
            profile_textview_dealer_since = findViewById(R.id.profile_textview_dealer_since);
            profile_textview_whatsapp_number = findViewById(R.id.profile_textview_whatsapp_number);
            profile_textview_gender = findViewById(R.id.profile_textview_gender);
            profile_textview_dob = findViewById(R.id.profile_textview_dob);
            profile_textview_facebook = findViewById(R.id.profile_textview_facebook);
            profile_textview_twitter = findViewById(R.id.profile_textview_twitter);
            profile_textview_instagram = findViewById(R.id.profile_textview_instagram);
            profile_textview_telegram = findViewById(R.id.profile_textview_telegram);
            profile_textview_doc_type = findViewById(R.id.profile_textview_doc_type);
            profile_textview_doc_number = findViewById(R.id.profile_textview_doc_number);

            profile_imageview_warning = findViewById(R.id.profile_imageview_warning);
            profile_imageview_doc_image = findViewById(R.id.profile_imageview_doc_image);

            profileActivity = this;
            getProfileDetails();
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getProfileDetails() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            if (userProfile != null) {
                setDataInViews();
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getProfile());
            getDataFromServer(parameter);
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getDataFromServer(RequestParameter parameter) {
        try {
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            Log.d("MyToken", token);

            Log.d(TAG, "token => " + token);
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri() + "?" + parameter.getEncodedParams(), this, this) {
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
            request.setShouldCache(false);
            GetServerData.addRequestToQueue(this, request);
        } catch (Exception e) {
            giveErrorOnError();
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
                giveErrorOnError();
            }

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        try {
            giveErrorOnError();
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
                userProfile = JSONParser.parseUserProfile(jsonObject1);
                if (userProfile != null) {
                    boolean b = ShPrefUserDetails.storeUserProfile(this, userProfile);
                    if (b) {
                        setDataInViews();
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
    }

    private void setDataInViews() {
        try {
            if (userProfile != null) {
                profile_textview_name.setText(userProfile.getName() + "");
                profile_textview_phone.setText(userProfile.getContact_no() + "");
                profile_textview_email.setText(userProfile.getEmail_id() + "");
                profile_textview_address.setText(userProfile.getAddress() + "");
                profile_textview_area.setText(userProfile.getArea() + "");
                profile_textview_state.setText(userProfile.getState() + "");
                profile_textview_city.setText(userProfile.getCity() + "");
                profile_textview_pincode.setText(userProfile.getPostal_code() + "");

                profile_textview_whatsapp_number.setText(userProfile.getWhatsappNumber() + "");
                //                profile_textview_dealer_since.setText(userProfile.get() + "");
                profile_textview_gender.setText(userProfile.getGender() + "");
                profile_textview_dob.setText(userProfile.getDate_of_birth() + "");

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

                profile_textview_facebook.setText(userProfile.getFacebook_link() + "");
                profile_textview_twitter.setText(userProfile.getTwitter_link() + "");
                profile_textview_instagram.setText(userProfile.getInstagram_link() + "");
                profile_textview_telegram.setText(userProfile.getTelegram_link() + "");

                profile_textview_doc_type.setText(userProfile.getDocument_type() + "");
                profile_textview_doc_number.setText(userProfile.getDocument_number() + "");

                try {
                    Log.d(TAG, AppURL.getProfileImageURL() + userProfile.getProfileImage());
                    if (!userProfile.getProfileImage().equals("")) {
                        if (userProfile.getProfileImageBitmap() != null) {
                            profile_imageview_image.setImageBitmap(userProfile.getProfileImageBitmap());
                        } else {
                            ImageRequest imageRequest = new ImageRequest(AppURL.getProfileImageURL() + userProfile.getProfileImage(),
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                                userProfile.setProfileImageBitmap(bitmap);
                                                profile_imageview_image.setImageBitmap(userProfile.getProfileImageBitmap());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    AppURLParams.imageWidthMerchant, AppURLParams.imageHeightMerchant, null, null);
                            GetServerData.addRequestToQueue(this, imageRequest);
                        }
                    } else {
                        profile_imageview_image.setImageResource(R.drawable.avatar);
                    }
                } catch (Exception e) {

                }

                try {
                    Log.d(TAG, AppURL.getDocumentImageURL() + userProfile.getDocumentImage());
                    if (!userProfile.getDocumentImage().equals("")) {
                        if (userProfile.getDocumentImageBitmap() != null) {
                            profile_imageview_doc_image.setImageBitmap(userProfile.getDocumentImageBitmap());
                        } else {
                            ImageRequest imageRequest = new ImageRequest(AppURL.getDocumentImageURL() + userProfile.getDocumentImage(),
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                                userProfile.setDocumentImageBitmap(bitmap);
                                                profile_imageview_doc_image.setImageBitmap(userProfile.getDocumentImageBitmap());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    AppURLParams.imageWidthMerchant, AppURLParams.imageHeightMerchant, null, null);
                            GetServerData.addRequestToQueue(this, imageRequest);
                        }
                    } else {
                        profile_imageview_doc_image.setImageResource(R.drawable.photo_doc);
                    }
                } catch (Exception e) {

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

    public static ProfileActivity getProfileActivity() {
        return profileActivity;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public void onBackPressed() {
        userProfile = null;
        profileActivity = null;
        if (profileUpdated) {
            setResult(301);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_view, menu);
        return true;
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
        } else if (id == R.id.action_profile_edit) {
            redirectUserEditProfile();
            return true;

        } else if (id == R.id.action_change_password) {
            redirectToChangePassword();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void redirectUserEditProfile() {
        try {
            Intent intent = new Intent(this, ProfileEditActivity.class);
            startActivityForResult(intent, 302);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectToChangePassword() {
        try {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == 303) {
                profileUpdated = true;
                userProfile = null;
                getProfileDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }
}
