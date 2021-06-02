package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fragment.UserClaimListViewFragment;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.model.UserClaim;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.observablescrollview.CacheFragmentStatePagerAdapter;
import com.observablescrollview.ObservableListView;
import com.observablescrollview.ObservableScrollViewCallbacks;
import com.observablescrollview.ScrollState;
import com.observablescrollview.ScrollUtils;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;
import com.widget.SlidingTabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserClaimHistoryListActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private CoordinatorLayout coordinatorLayout;
    private View mHeaderView;
    private Toolbar mToolbarView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private LinearLayout nodata_LL;
    private TextView nodata_textview;

    private ProgressDialog progress;
    private static final String TAG = "UserClaimHistory";
    private static UserClaimHistoryListActivity activity;

    private ArrayList<String> sliderTitleList;
    private HashMap<String, ArrayList<UserClaim>> listHashMap;
    private UserClaim selectedUserClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_history_list);

        try {
            mToolbarView = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbarView);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            nodata_LL = findViewById(R.id.nodata_LL);
            nodata_textview = findViewById(R.id.nodata_textview);

            activity = this;
            setupPageSlider();

            if (listHashMap == null) {
                getRequirementHistoryList();
            } else {
                setPageSlider();
            }

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void setupPageSlider() {
        try {
            mHeaderView = findViewById(R.id.header);
            ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
            mHeaderView.bringToFront();
            mPager = findViewById(R.id.pager);

            slidingTabLayout = findViewById(R.id.sliding_tabs);
            slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
            slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.bg_color_white));
            slidingTabLayout.setDistributeEvenly(true);
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getRequirementHistoryList() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getUserClaimHistory());
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
                                        processResponse(jsonObject);

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

    private void processResponse(JSONObject jsonObject) {
        try {
            if (progress != null) {
                progress.dismiss();
            }

            JSONArray jsonArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONArray(AppURLParams.data) : null;
            listHashMap = JSONParser.parseAllUserClaimMap(jsonArray);

            if (listHashMap != null) {
                if (listHashMap.size() > 0) {
                    sliderTitleList = new ArrayList<>();
                    for (String key : listHashMap.keySet()) {
                        if (!sliderTitleList.contains(key)) {
                            sliderTitleList.add(key);
                        }
                    }
                    Collections.sort(sliderTitleList);

                    mPager.setVisibility(View.VISIBLE);
                    nodata_LL.setVisibility(View.GONE);
                    setPageSlider();

                } else {
                    sliderTitleList = null;
                    listHashMap = null;
                    giveErrorOnNoData();
                }

            } else {
                listHashMap = null;
                giveErrorOnNoData();
            }
        } catch (Exception e) {
            listHashMap = null;
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void setPageSlider() {
        try {
            mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), sliderTitleList);
            mPager.setAdapter(mPagerAdapter);
            slidingTabLayout.setViewPager(mPager);

            // When the page is selected, other fragments' scrollY should be adjusted
            // according to the toolbar status(shown/hidden)
            slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int i) {
//                    propagateToolbarState(toolbarIsShown());
                    showToolbar();
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });

            propagateToolbarState(toolbarIsShown());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserClaimHistoryListActivity getActivity() {
        return activity;
    }

    private static class MyPagerAdapter extends CacheFragmentStatePagerAdapter {

        private static ArrayList<String> TITLES;
        private int mScrollY;

        public MyPagerAdapter(FragmentManager fm, ArrayList<String> list) {
            super(fm);
            TITLES = list;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = new UserClaimListViewFragment();
            Bundle args = new Bundle();
            args.putInt(UserClaimListViewFragment.ARG_TAB_INDEX, position);
            if (0 < mScrollY) {
                args.putInt(UserClaimListViewFragment.ARG_INITIAL_POSITION, 1);
            }
            f.setArguments(args);

            return f;
        }

        @Override
        public int getCount() {
            return TITLES.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String s = TITLES.get(position);
            if (s.equalsIgnoreCase(AppURLParams.statusVal0)) {
                return activity.getString(R.string.live_cliam);
            } else {
                return activity.getString(R.string.past_claim);
            }
        }
    }

    public HashMap<String, ArrayList<UserClaim>> getListHashMap() {
        return listHashMap;
    }

    public void setSelectedUserClaim(UserClaim selectedUserClaim) {
        this.selectedUserClaim = selectedUserClaim;
    }

    public UserClaim getSelectedUserClaim() {
        return selectedUserClaim;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == 51) {
                listHashMap = null;
                getRequirementHistoryList();

            } else if (resultCode == 61) {
                listHashMap = null;
                getRequirementHistoryList();

            } else if (resultCode == 71) {
                listHashMap = null;
                getRequirementHistoryList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        activity = null;
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

    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            mPager.setVisibility(View.GONE);
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
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            mPager.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = mToolbarView.getHeight();
        final ObservableListView fragment_observable_listview = (ObservableListView) view.findViewById(R.id.fragment_observable_listview);
        if (fragment_observable_listview == null) {
            return;
        }
        int scrollY = fragment_observable_listview.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = mToolbarView.getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        ObservableListView fragment_observable_listview = (ObservableListView) view.findViewById(R.id.fragment_observable_listview);
        if (fragment_observable_listview == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < fragment_observable_listview.getCurrentScrollY()) {
                fragment_observable_listview.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding)
            if (fragment_observable_listview.getCurrentScrollY() < toolbarHeight) {
                fragment_observable_listview.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }
}
