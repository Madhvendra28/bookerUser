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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.MoneyRequestHistoryListAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.MoneyRequestActivity;
import com.bookkr.user.R;
import com.google.android.material.snackbar.Snackbar;
import com.model.MoneyRequest;
import com.model.RequestParameter;
import com.observablescrollview.ObservableListView;
import com.observablescrollview.ObservableScrollViewCallbacks;
import com.observablescrollview.ScrollUtils;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;


public class MoneyRequestListViewFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    int mNum;
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    public static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    private CoordinatorLayout coordinatorLayout;
    private ObservableListView fragment_observable_listview;
    private LinearLayout nodata_LL;
    private TextView nodata_textview;

    private ProgressDialog progress;
    public static final String TAG = "MoneyRequestFragment";
    private MoneyRequestActivity activity;

    private ArrayList<MoneyRequest> moneyRequestHistoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mNum = getArguments() != null ? getArguments().getInt(ARG_TAB_INDEX) : -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_listview_obserable_items, container, false);
            Activity parentActivity = getActivity();
            activity = (MoneyRequestActivity) getActivity();

            coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
            fragment_observable_listview = view.findViewById(R.id.fragment_observable_listview);
            fragment_observable_listview.addHeaderView(inflater.inflate(R.layout.padding, fragment_observable_listview, false));

            nodata_LL = view.findViewById(R.id.nodata_LL);
            nodata_textview = view.findViewById(R.id.nodata_textview);

            try {
                if (moneyRequestHistoryList == null) {
                    getMoneyRequestHistoryList();
                } else {
                    setDataInListAdapter();
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

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void refreshMoneyRequestData(){
        Log.d(TAG, "refreshMoneyRequetData 2");
        moneyRequestHistoryList = null;
        getMoneyRequestHistoryList();
    }

    private void getMoneyRequestHistoryList() {
        try {
            if (!ConnectionManager.isOnline(activity)) {
                ConnectionManager.createDialog(activity);
                Log.d("Network state", ConnectionManager.isOnline(activity) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(activity);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getMoneyRequestHistory());
            getDataFromServer(parameter);
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getDataFromServer(RequestParameter parameter) {
        try {
            final LinkedHashMap<String, String> params = parameter.getParams();
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final String token = ShPrefUserDetails.getToken(activity);

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
            GetServerData.addRequestToQueue(activity, request);
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

            JSONArray jsonArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONArray(AppURLParams.data) : null;
            moneyRequestHistoryList = JSONParser.parseMoneyRequest(jsonArray);

            if (moneyRequestHistoryList != null) {
                setDataInListAdapter();

            } else {
                moneyRequestHistoryList = null;
                giveErrorOnNoData();
            }
        } catch (Exception e) {
            moneyRequestHistoryList = null;
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void setDataInListAdapter() {
        try {
            if (moneyRequestHistoryList != null && moneyRequestHistoryList.size() > 0) {
                fragment_observable_listview.setVisibility(View.VISIBLE);
                nodata_LL.setVisibility(View.GONE);
                nodata_textview.setVisibility(View.GONE);

                MoneyRequestHistoryListAdapter adapter = new MoneyRequestHistoryListAdapter(activity, moneyRequestHistoryList);
                fragment_observable_listview.setAdapter(adapter);

            } else {
                giveErrorOnNoData();
            }
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
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
            if (progress != null) {
                progress.dismiss();
            }
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
