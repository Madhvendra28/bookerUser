package com.bookkr.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fragment.DashboardHomeFragment;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.model.Event;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;


public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbarView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FrameLayout container_FL;
    private LinearLayout nodata_LL;
    private TextView nodata_textview;
    private SearchView searchView;

    private boolean userSignout = false;
    private ProgressDialog progress;
    private static final String TAG = "Home Activity";
    private int requestFor = -1; // 1 = check status, 2 = merchant list,
    //hii this is by me
    private DashboardHomeFragment dashboardHomeFragment;

    private String userStatus = null, queryStr = "";
    private String wallet_amount = "", claimed_quantity = "", confirmed_quantity = "", pay_fail = "",
            open_orders = "", invested_amount = "", pending_amount = "";

    private static HomeActivity activity;
    private Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            mToolbarView = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbarView);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            container_FL = findViewById(R.id.container_FL);
            nodata_LL = findViewById(R.id.nodata_LL);
            nodata_textview = findViewById(R.id.nodata_textview);

            activity = this;
            setDrawer();

            if (userStatus == null) {
                //getUserStatus();
                getUserBalAndDetails();
            }

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public static HomeActivity getActivity() {
        return activity;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    private void getUserStatus() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            if (userStatus != null) {
                return;
            }
            nodata_LL.setVisibility(View.GONE);

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
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
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserCheckStatus());
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri(),
                    new Response.Listener<String>() {
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
                                        try {
                                            JSONObject jsonObject1 = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONObject(AppURLParams.data) : null;
                                            if (jsonObject1 != null) {
                                                userStatus = !jsonObject1.getString(AppURLParams.status).equals("null") ? jsonObject1.getString(AppURLParams.status) : null;

                                                if (userStatus != null) {
                                                    if (userStatus.equals(AppURLParams.statusVal1)) {
                                                        reachToHome();

                                                    } else {
                                                        Toast.makeText(HomeActivity.this, HomeActivity.this.getString(R.string.error_user_deactivated), Toast.LENGTH_LONG).show();
                                                        userLogoutLocal();
                                                    }
                                                } else {
                                                    Toast.makeText(HomeActivity.this, HomeActivity.this.getString(R.string.error_user_deactivated), Toast.LENGTH_LONG).show();
                                                    userLogoutLocal();
                                                }

                                            } else {
                                                giveErrorOnNoData();
                                            }
                                        } catch (Exception e) {
                                            giveErrorOnError();
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
                    volleyError.printStackTrace();
                }
            }) {
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
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getUserBalAndDetails() {
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

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserDashboard());
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri(),
                    new Response.Listener<String>() {
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
                                        try {
                                            JSONObject jsonObject1 = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONObject(AppURLParams.data) : null;
                                            if (jsonObject1 != null) {
                                                userStatus = !jsonObject1.getString(AppURLParams.status).equals("null") ? jsonObject1.getString(AppURLParams.status) : null;
                                                wallet_amount = !jsonObject1.getString(AppURLParams.wallet_amount).equals("null") ? jsonObject1.getString(AppURLParams.wallet_amount) : "";
                                                claimed_quantity = !jsonObject1.getString(AppURLParams.claimed_quantity).equals("null") ? jsonObject1.getString(AppURLParams.claimed_quantity) : "";
                                                confirmed_quantity = !jsonObject1.getString(AppURLParams.confirmed_quantity).equals("null") ? jsonObject1.getString(AppURLParams.confirmed_quantity) : "";
                                                pay_fail = !jsonObject1.getString(AppURLParams.pay_fail).equals("null") ? jsonObject1.getString(AppURLParams.pay_fail) : "";
                                                open_orders = !jsonObject1.getString(AppURLParams.open_orders).equals("null") ? jsonObject1.getString(AppURLParams.open_orders) : "";
                                                invested_amount = !jsonObject1.getString(AppURLParams.invested_amount).equals("null") ? jsonObject1.getString(AppURLParams.invested_amount) : "";
                                                pending_amount = !jsonObject1.getString(AppURLParams.pending_amount).equals("null") ? jsonObject1.getString(AppURLParams.pending_amount) : "";

                                                if (userStatus != null) {
                                                    if (userStatus.equals(AppURLParams.statusVal1)) {
                                                        reachToHome();

                                                    } else {
                                                        Toast.makeText(HomeActivity.this, HomeActivity.this.getString(R.string.error_user_deactivated), Toast.LENGTH_LONG).show();
                                                        userLogoutLocal();
                                                    }
                                                } else {
                                                    Toast.makeText(HomeActivity.this, HomeActivity.this.getString(R.string.error_user_deactivated), Toast.LENGTH_LONG).show();
                                                    userLogoutLocal();
                                                }

                                                setDrawerTitleContent();
                                                setHomeFragmentDataCards();

                                            } else {
                                                giveErrorOnNoData();
                                            }
                                        } catch (Exception e) {
                                            giveErrorOnError();
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
                    volleyError.printStackTrace();
                }
            }) {
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
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void initBottomBarOnClick() {
        try {
            RelativeLayout bottom_tab_RL_home = findViewById(R.id.bottom_tab_RL_home);
            RelativeLayout bottom_tab_RL_requirement = findViewById(R.id.bottom_tab_RL_requirement);
            RelativeLayout bottom_tab_RL_chat = findViewById(R.id.bottom_tab_RL_chat);
            RelativeLayout bottom_tab_RL_payment = findViewById(R.id.bottom_tab_RL_payment);
            RelativeLayout bottom_tab_RL_order = findViewById(R.id.bottom_tab_RL_order);

            final ImageView bottom_tab_imageview_home = findViewById(R.id.bottom_tab_imageview_home);
            final ImageView bottom_tab_imageview_requirement = findViewById(R.id.bottom_tab_imageview_requirement);
            final ImageView bottom_tab_imageview_message = findViewById(R.id.bottom_tab_imageview_message);
            final ImageView bottom_tab_imageview_payment = findViewById(R.id.bottom_tab_imageview_payment);


            bottom_tab_RL_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottom_tab_imageview_home.setImageResource(R.drawable.ic_action_home_red);
                    bottom_tab_imageview_requirement.setImageResource(R.drawable.box_icon);
                    bottom_tab_imageview_message.setImageResource(R.drawable.message_icon);
                    bottom_tab_imageview_payment.setImageResource(R.drawable.payment_icon);
                    bottom_tab_imageview_message.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icon_color));

                    reachToHome();
                }
            });

            bottom_tab_RL_requirement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bottom_tab_imageview_home.setImageResource(R.drawable.home_icon);
                    bottom_tab_imageview_requirement.setImageResource(R.drawable.ic_action_box_red);
                    bottom_tab_imageview_message.setImageResource(R.drawable.message_icon);
                    bottom_tab_imageview_payment.setImageResource(R.drawable.payment_icon);
                    bottom_tab_imageview_message.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icon_color));

                    redirectPostRequirements();
                }
            });

            bottom_tab_RL_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottom_tab_imageview_home.setImageResource(R.drawable.home_icon);
                    bottom_tab_imageview_requirement.setImageResource(R.drawable.box_icon);
                    bottom_tab_imageview_message.setImageResource(R.drawable.message_icon);
                    bottom_tab_imageview_payment.setImageResource(R.drawable.payment_icon);
                    bottom_tab_imageview_message.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icon_color));

                    viewOrderHistory(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reachToHome() {
        initBottomBarOnClick();
        //getUserBalAndDetails();
        HomeFragment();
    }

    public void HomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dashboardHomeFragment = new DashboardHomeFragment();
        fragmentTransaction.replace(R.id.container_FL, dashboardHomeFragment, AppURLParams.fragmentHome).commit();
    }

    private void setHomeFragmentDataCards() {
        if (dashboardHomeFragment != null) {
            dashboardHomeFragment.setDataCardsForStats(claimed_quantity, confirmed_quantity, pay_fail, open_orders, invested_amount, pending_amount);
        }
    }

    public void redirectPostRequirements() {
        viewPostRequirements(null);
    }

    public void redirectClaimHistory() {
        viewUserClaimHistory();
    }

    public void viewProfile(View view) {
        try {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, 300);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPostRequirements(View view) {
        try {
            Intent intent = new Intent(this, RequirementListActivity.class);
            startActivityForResult(intent, 30);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewUserClaimHistory() {
        try {
           // Intent intent = new Intent(this, UserClaimHistoryListActivity.class);
            Intent intent = new Intent(this, ClaimUserHistoryDetialActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewOrderHistory(View view) {
        try {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(View view) {
        try {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToRateUs() {
        try {

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void shareApp() {
        try {
            startActivity(new Intent(HomeActivity.this,ReferandEarn.class));
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void userLogoutLocal() {
        try {
            boolean b = ShPrefUserDetails.removeUserDataAfterSignout(this);
            if (b) {
                userSignout = true;
                onBackPressed();
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_signout_unsuccessful), Snackbar.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {

            int count = getFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                setDrawerEnabled(true);

            } else if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START);

            } else if (!queryStr.equals("")) {
                try {
                    searchView.setQuery("", false); // clear the text
                    searchView.setIconified(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (userSignout) {
                setResult(200);
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);

            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 202) {

            } else if (resultCode == 31) {
                reachToHome();

            } else if (resultCode == 301) {
                setDrawerTitleContent();

            } else if (resultCode == 351) {

            } else if (resultCode == 151) {

            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setDrawer() {
        try {
            //Initializing NavigationView
            navigationView = findViewById(R.id.navigation_view);
            disableNavigationViewScrollbars(navigationView);
            setDrawerTitleContent();

            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu

            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    try {
                        //Closing drawer on item click
                        drawerLayout.closeDrawers();
                        Uri uri = null;
                        Intent intent = null;
                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            case R.id.drawer_profile:
                                viewProfile(null);
                                return false;
                            case R.id.drawer_claim_history:
                                Log.d("serajdata","claim history clicked");
                               startActivity(new Intent(HomeActivity.this, ClaimUserHistoryDetialActivity.class));
                                return false;

                            case R.id.drawer_rate_chart:
                                return false;
                            case R.id.drawer_wallet_details:
                                startActivity(new Intent(HomeActivity.this, WalletBankDetailActivity.class));
                                return false;
                            case R.id.drawer_address_generator:
                                return false;
                            case R.id.drawer_order_history:
                                viewOrderHistory(null);
                                return false;

                            case R.id.drawer_saved_items:
                                return false;
                            case R.id.drawer_drop_price:
                                return false;
                            case R.id.drawer_buy_address_saved:
                                return false;
                            case R.id.drawer_feedback_suggestion:
                                return false;

                            case R.id.drawer_about_us:
                                uri = Uri.parse("http://google.com/admin/about-us.php");
                                intent = new Intent(Intent.ACTION_VIEW, uri);

                                int flags = intent.getFlags();
                                if ((flags == 0 & Intent.FLAG_GRANT_READ_URI_PERMISSION == 0) &&
                                        (flags == 0 & Intent.FLAG_GRANT_WRITE_URI_PERMISSION == 0)) {
                                    // redirect the nested Intent
                                    startActivity(intent);
                                }
                                startActivity(intent);
                                return false;

                            case R.id.drawer_contact_us:
                                uri = Uri.parse("http://google.com/admin/contact-us.php");
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                return false;
                            case R.id.drawer_privacy_policies:
                                uri = Uri.parse("http://google.com/admin/privacy-policy.php");
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                return false;
                            case R.id.drawer_faq:
                                return false;

                            case R.id.drawer_rate_us:
                                goToRateUs();
                                return false;
                            case R.id.drawer_share_earn:
                                shareApp();
                                return false;
                            case R.id.drawer_signout:
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setMessage(R.string.dialog_user_signout);
                                builder.setPositiveButton("No", null);
                                builder.setNegativeButton("Signout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        userLogoutLocal();
                                    }
                                });
                                builder.setCancelable(true);
                                builder.show();
                                return false;
                            default:
                                Snackbar.make(coordinatorLayout, "Invalid Selection", Snackbar.LENGTH_SHORT).show();
                                return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });

            // Initializing Drawer Layout and ActionBarToggle
            drawerLayout = findViewById(R.id.drawer);
            actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, mToolbarView, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    super.onDrawerOpened(drawerView);
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) HomeActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(HomeActivity.this.getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            //Setting the actionbarToggle to drawer layout
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDrawerTitleContent() {
        try {
            navigationView = findViewById(R.id.navigation_view);
            View headerLayout = navigationView.getHeaderView(0);
            final ImageView drawer_header_imageview_image = headerLayout.findViewById(R.id.drawer_header_imageview_image);
            ImageView drawer_header_imageview_warning = headerLayout.findViewById(R.id.drawer_header_imageview_warning);
            TextView drawer_header_textview_name = headerLayout.findViewById(R.id.drawer_header_textview_name);
            TextView drawer_header_textview_code = headerLayout.findViewById(R.id.drawer_header_textview_code);
            TextView drawer_header_textview_wallet_balance = headerLayout.findViewById(R.id.drawer_header_textview_wallet_balance);

            String userCode = ShPrefUserDetails.getUser_code(this);
            String name = ShPrefUserDetails.getName(this);
            String profileImage = ShPrefUserDetails.getProfile_image(this);
            String verificationStatus = ShPrefUserDetails.getVerification_status(this);

            if (userCode != null) {
                drawer_header_textview_code.setText(userCode);
            }

            if (name != null) {
                drawer_header_textview_name.setText(name);
            }

            if (verificationStatus.equals(AppURLParams.statusVal1)) {
                drawer_header_imageview_warning.setVisibility(View.GONE);
            } else {
                drawer_header_imageview_warning.setVisibility(View.VISIBLE);
            }

            if (!wallet_amount.equals("")) {
                drawer_header_textview_wallet_balance.setText(getString(R.string.rupees) + " " + wallet_amount);
            } else {
                drawer_header_textview_wallet_balance.setText(wallet_amount);
            }

            try {
                if (!profileImage.equals("")) {
                    Glide.with(drawer_header_imageview_image).load(
                            AppURL.getProfileImageURL() + profileImage)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    drawer_header_imageview_image.setImageDrawable(resource);
                                }
                            });
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        try {
            if (navigationView != null) {
                NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
                if (navigationMenuView != null) {
                    navigationMenuView.setVerticalScrollBarEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        try {
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            container_FL.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_no_data_found));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnError() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            container_FL.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
