/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.ShipOrderHistoryListAdapter;
import com.bookkr.user.OrderHistoryActivity;
import com.bookkr.user.OrderHistoryDetailsActivity;
import com.bookkr.user.R;
import com.google.android.material.snackbar.Snackbar;
import com.model.ShipOrderDetails;
import com.observablescrollview.ObservableListView;
import com.observablescrollview.ObservableScrollViewCallbacks;
import com.observablescrollview.ScrollUtils;

import java.util.ArrayList;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;


public class OrderHistoryListViewFragment extends Fragment {

    int mNum;
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    private CoordinatorLayout coordinatorLayout;
    private ObservableListView fragment_observable_listview;
    private LinearLayout nodata_LL;
    private TextView nodata_textview;

    private OrderHistoryActivity activity;
    public static final String TAG = "OrderHistory";
    private ArrayList<ShipOrderDetails> shipOrderDetailsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mNum = getArguments() != null ? getArguments().getInt(ARG_TAB_INDEX) : -1;
            String s = OrderHistoryActivity.getActivity().getSliderTitleList().get(mNum);
            shipOrderDetailsArrayList = OrderHistoryActivity.getActivity().getListHashMap().get(s + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_listview_obserable_items_2, container, false);
            Activity parentActivity = getActivity();
            activity = (OrderHistoryActivity) getActivity();

            coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
            fragment_observable_listview = view.findViewById(R.id.fragment_observable_listview);
            fragment_observable_listview.addHeaderView(inflater.inflate(R.layout.padding, fragment_observable_listview, false));

            nodata_LL = view.findViewById(R.id.nodata_LL);
            nodata_textview = view.findViewById(R.id.nodata_textview);

            try {
                Log.d("mNum", mNum + "");
                String s = OrderHistoryActivity.getActivity().getSliderTitleList().get(mNum);
                shipOrderDetailsArrayList = OrderHistoryActivity.getActivity().getListHashMap().get(s + "");

                if (shipOrderDetailsArrayList != null) {
                    fragment_observable_listview.setVisibility(View.VISIBLE);
                    nodata_LL.setVisibility(View.GONE);
                    nodata_textview.setVisibility(View.GONE);

                    ShipOrderHistoryListAdapter adapter = new ShipOrderHistoryListAdapter(parentActivity, shipOrderDetailsArrayList);
                    fragment_observable_listview.setAdapter(adapter);

                } else {
                    fragment_observable_listview.setVisibility(View.GONE);
                    nodata_LL.setVisibility(View.VISIBLE);
                    nodata_textview.setVisibility(View.VISIBLE);
                    nodata_textview.setText(activity.getString(R.string.error_no_data_found));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (parentActivity instanceof ObservableScrollViewCallbacks) {
                // Scroll to the specified position after layout
                Bundle args = getArguments();
                if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                    final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
//                    Log.d("in fragment", "initial position "+initialPosition);
                    ScrollUtils.addOnGlobalLayoutListener(fragment_observable_listview, new Runnable() {
                        @Override
                        public void run() {
                            // scrollTo() doesn't work, should use setSelection()
                            fragment_observable_listview.setSelection(initialPosition);
                        }
                    });
                }

                // TouchInterceptionViewGroup should be a parent view other than ViewPager.
                // This is a workaround for the issue #117:
                // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
                fragment_observable_listview.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.coordinatorLayout));
                fragment_observable_listview.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
            }

            fragment_observable_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        listClickHandler(i - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void listClickHandler(int position) {
        try {
            if (shipOrderDetailsArrayList != null && shipOrderDetailsArrayList.size() > 0) {
                ShipOrderDetails shipOrderDetails = shipOrderDetailsArrayList.get(position);
                activity.setSelectedShipOrderDetails(shipOrderDetails);

                Intent intent = new Intent(activity, OrderHistoryDetailsActivity.class);
                activity.startActivityForResult(intent, 50);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            } else {
                Snackbar.make(coordinatorLayout, activity.getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            activity.setSelectedShipOrderDetails(null);
            Snackbar.make(coordinatorLayout, activity.getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void giveErrorOnNoData() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            fragment_observable_listview.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setVisibility(View.VISIBLE);
            nodata_textview.setText(activity.getString(R.string.error_no_data_found));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnError() {
        try {
//            if (progress != null) {
//                progress.dismiss();
//            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            fragment_observable_listview.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setVisibility(View.VISIBLE);
            nodata_textview.setText(activity.getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
