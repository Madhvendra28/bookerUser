package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.fragment.BankDetailsListViewFragment;
import com.fragment.MoneyRequestListViewFragment;
import com.google.android.material.snackbar.Snackbar;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.observablescrollview.CacheFragmentStatePagerAdapter;
import com.observablescrollview.ObservableListView;
import com.observablescrollview.ObservableScrollViewCallbacks;
import com.observablescrollview.ScrollState;
import com.observablescrollview.ScrollUtils;
import com.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class MoneyRequestActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private CoordinatorLayout coordinatorLayout;
    private View mHeaderView;
    private Toolbar mToolbarView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

//    private LinearLayout nodata_LL;
//    private TextView nodata_textview;

    private int requestFor = -1;
    private ProgressDialog progress;
    private final String TAG = "MoneyRequestActivity";

    private static MoneyRequestActivity activity;
    private ArrayList<String> sliderTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_request);

        try {
            mToolbarView = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbarView);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            activity = this;
            setupPageSlider();

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

            sliderTitleList = new ArrayList<>();
            sliderTitleList.add(getString(R.string.request_history));
            sliderTitleList.add(getString(R.string.bank_details));

            mPager.setVisibility(View.VISIBLE);
            setPageSlider();
        } catch (Exception e) {
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

    public static MoneyRequestActivity getActivity() {
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
            Fragment f = null;

            if (position == 0) {
                f = new MoneyRequestListViewFragment();
                Bundle args = new Bundle();
                args.putInt(MoneyRequestListViewFragment.ARG_TAB_INDEX, position);
                if (0 < mScrollY) {
                    args.putInt(MoneyRequestListViewFragment.ARG_INITIAL_POSITION, 1);
                }
                f.setArguments(args);

            } else if (position == 1) {
                f = new BankDetailsListViewFragment();
                Bundle args = new Bundle();
                args.putInt(BankDetailsListViewFragment.ARG_TAB_INDEX, position);
                if (0 < mScrollY) {
                    args.putInt(BankDetailsListViewFragment.ARG_INITIAL_POSITION, 1);
                }
                f.setArguments(args);
            }

            return f;
        }

        @Override
        public int getCount() {
            return TITLES.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == 302) {
                setPageSlider();
                /*mPager.setCurrentItem(0);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
                Log.d(TAG, mPager.getCurrentItem() + ", " + fragment);

                if (mPager.getCurrentItem() == 0 && fragment != null) {
                    Log.d(TAG, "refreshMoneyRequetData 1");
                    ((MoneyRequestListViewFragment) fragment).refreshMoneyRequestData();
                }*/

//                FragmentManager fm = getSupportFragmentManager();
//                MoneyRequestListViewFragment f = (MoneyRequestListViewFragment) fm.getBackStackEntryAt(0);
//                f.refreshMoneyRequetData();

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
