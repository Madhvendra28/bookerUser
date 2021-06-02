package com.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.DashboardEventsListAdapter;
import com.adapter.OfferBannerImageSliderAdapter;
import com.adapter.UpdateProfileAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.HomeActivity;
import com.bookkr.user.R;
import com.google.android.material.snackbar.Snackbar;
import com.listnerr.RemoveOneItemsListener;
import com.model.Event;
import com.model.OfferBanner;
import com.model.RequestParameter;
import com.model.UpdateProfile;
import com.preferences.ShPrefUserDetails;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
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
import java.util.List;
import java.util.Map;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

/**
 * Created by ANKIT on 27-Jul-20.
 */
public class DashboardHomeFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private LinearLayout fragment_LL_ongoing_events, fragment_LL_upcoming_events, fragment_LL_claim_quantity, fragment_LL_confirm_quantity,
            fragment_LL_failed_quantity;
//    fragment_LL_all_requirements, fragment_LL_my_claimed_list, fragment_LL_post_requirement

    private RecyclerView fragment_recyclerview_update_profile, fragment_recyclerview_events;
    private ScrollingPagerIndicator fragment_page_indicator_events;
    private TextView fragment_textview_claim_quantity, fragment_textview_confirm_quantity, fragment_textview_failed_quantity,
            fragment_textview_open_orders, fragment_textview_invested_amount, fragment_textview_pending_amount;

    private View view;
    private int requestFor = -1;
    private ProgressDialog progress;
    public static final String TAG = "DashboardHomeFragment";
    private String callingParam;
    private HomeActivity activity;

    private ArrayList<OfferBanner> offerBanners;
    public static List<UpdateProfile> ProfileList;

    private ArrayList<Event> onGoingEventArrayList, upComingEventArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callingParam = getArguments() != null ? getArguments().getString(AppURLParams.type) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            activity = (HomeActivity) getActivity();
            view = inflater.inflate(R.layout.fragment_dashboard_home, container, false);
            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

//            fragment_LL_all_requirements = view.findViewById(R.id.fragment_LL_all_requirements);
//            fragment_LL_my_claimed_list = view.findViewById(R.id.fragment_LL_my_claimed_list);
            fragment_LL_ongoing_events = view.findViewById(R.id.fragment_LL_ongoing_events);
            fragment_LL_upcoming_events = view.findViewById(R.id.fragment_LL_upcoming_events);
//            fragment_LL_post_requirement = view.findViewById(R.id.fragment_LL_post_requirement);
            fragment_LL_claim_quantity = view.findViewById(R.id.fragment_LL_claim_quantity);
            fragment_LL_confirm_quantity = view.findViewById(R.id.fragment_LL_confirm_quantity);
            fragment_LL_failed_quantity = view.findViewById(R.id.fragment_LL_failed_quantity);

            fragment_recyclerview_events = view.findViewById(R.id.fragment_recyclerview_events);
            fragment_page_indicator_events = view.findViewById(R.id.fragment_page_indicator_events);

            fragment_textview_claim_quantity = view.findViewById(R.id.fragment_textview_claim_quantity);
            fragment_textview_confirm_quantity = view.findViewById(R.id.fragment_textview_confirm_quantity);
            fragment_textview_failed_quantity = view.findViewById(R.id.fragment_textview_failed_quantity);
            fragment_textview_open_orders = view.findViewById(R.id.fragment_textview_open_orders);
            fragment_textview_invested_amount = view.findViewById(R.id.fragment_textview_invested_amount);
            fragment_textview_pending_amount = view.findViewById(R.id.fragment_textview_pending_amount);

            fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_selected_round_button);
            fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_round_button);
//            fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_round_button);
            fragment_page_indicator_events.setVisibility(View.GONE);
            initViewOfUpdateProfile(view);

            initializeEventButtonClick();

            if (offerBanners == null) {
                getOfferBannerList();
            } else {
                setOfferBannerImages();
            }

            if (onGoingEventArrayList == null || upComingEventArrayList == null) {
                getEventsList();
            } else {
                setDataInEventAdapter();
            }


            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initViewOfUpdateProfile(View view) {
        try {
            fragment_recyclerview_update_profile = view.findViewById(R.id.fragment_recyclerview_update_profile);
            fragment_recyclerview_update_profile.setHasFixedSize(true);
            ProfileList = new ArrayList<>();

            UpdateProfile updateProfile = new UpdateProfile();
            updateProfile.setImageLink(String.valueOf(R.drawable.dummy_profile));
            updateProfile.setUpdate(activity.getString(R.string.update_profile));
            ProfileList.add(updateProfile);

            UpdateProfile updateProfile1 = new UpdateProfile();
            updateProfile1.setImageLink(String.valueOf(R.drawable.dummy_profile));
            updateProfile.setUpdate(activity.getString(R.string.how_to_use));
            ProfileList.add(updateProfile1);

            UpdateProfile updateProfile2 = new UpdateProfile();
            updateProfile2.setImageLink(String.valueOf(R.drawable.dummy_profile));
            updateProfile.setUpdate(activity.getString(R.string.update_profile));
            ProfileList.add(updateProfile2);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
            final UpdateProfileAdapter updateProfileAdapter = new UpdateProfileAdapter(getContext(), ProfileList);
            updateProfileAdapter.setRemoveOneItemsListener(new RemoveOneItemsListener() {
                @Override
                public void removeItem(int pos) {
                    ProfileList.remove(pos);
                    updateProfileAdapter.notifyDataSetChanged();
                }
            });
            fragment_recyclerview_update_profile.setLayoutManager(gridLayoutManager);
            fragment_recyclerview_update_profile.setAdapter(updateProfileAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeEventButtonClick() {
        try {
           /* fragment_LL_all_requirements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.redirectPostRequirements();
                }
            });

            fragment_LL_my_claimed_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.redirectClaimHistory();
                }
            });*/

            fragment_LL_ongoing_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_selected_round_button);
                    fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_round_button);
//                    fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_round_button);

                    setDataInOngoingEventAdapter();
                }
            });

            fragment_LL_upcoming_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_round_button);
                    fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_selected_round_button);
//                    fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_round_button);

                    setDataInUpcomingEventAdapter();
                }
            });

            fragment_LL_claim_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.redirectClaimHistory();
                }
            });

            fragment_LL_confirm_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.redirectClaimHistory();
                }
            });

            fragment_LL_failed_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.redirectClaimHistory();
                }
            });


            /*fragment_LL_post_requirement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_round_button);
                    fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_round_button);
                    fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_selected_round_button);

                    Intent intent = new Intent(activity, RequirementAddActivity.class);
//                Gson gson = new Gson();
//                intent.putExtra("data", gson.toJson(event));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOfferBannerList() {
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

            requestFor = 1;
//            progress = new ProgressDialog(activity);
//            progress.setMessage(getString(R.string.progress_getting_data));
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getBannerList());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
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
                                            JSONArray jsonArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONArray(AppURLParams.data) : null;
                                            offerBanners = JSONParser.parseOfferBanner(jsonArray);
                                            if (offerBanners != null) {
                                                setOfferBannerImages();

                                            } else {
                                                offerBanners = null;
                                            }
                                        } catch (Exception e) {
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
            GetServerData.addRequestToQueue(activity, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEventsList() {
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

            requestFor = 2;
//            progress = new ProgressDialog(activity);
//            progress.setMessage(getString(R.string.progress_getting_data));
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getAllEventDetailsUser());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(activity);
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
                                            JSONArray jsonArray1 = !jsonObject.isNull(AppURLParams.onGoingEvent) ? jsonObject.getJSONArray(AppURLParams.onGoingEvent) : null;
                                            JSONArray jsonArray2 = !jsonObject.isNull(AppURLParams.upComingEvent) ? jsonObject.getJSONArray(AppURLParams.upComingEvent) : null;

                                            if (jsonArray1 != null) {
                                                onGoingEventArrayList = JSONParser.parseEventList(jsonArray1);
                                            } else {
                                                onGoingEventArrayList = null;
                                            }

                                            if (jsonArray2 != null) {
                                                upComingEventArrayList = JSONParser.parseEventList(jsonArray2);
                                            } else {
                                                upComingEventArrayList = null;
                                            }

                                            if (onGoingEventArrayList != null || upComingEventArrayList != null) {
                                                setDataInEventAdapter();
                                            } else {
                                                fragment_recyclerview_events.setVisibility(View.GONE);
                                                fragment_page_indicator_events.setVisibility(View.GONE);
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
            GetServerData.addRequestToQueue(activity, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOfferBannerImages() {
        try {
            final SliderView sliderView = view.findViewById(R.id.fragment_home_image_slider);
            if (offerBanners != null && offerBanners.size() > 0) {
                OfferBannerImageSliderAdapter adapter = new OfferBannerImageSliderAdapter(activity, offerBanners);
                sliderView.setSliderAdapter(adapter);

                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                // set indicator animation by using SliderLayout.IndicatorAnimations.
                // :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//            sliderView.setIndicatorSelectedColor(Color.WHITE);
//            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                sliderView.startAutoCycle();

            } else {
                sliderView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInEventAdapter() {
        try {
            if (onGoingEventArrayList != null && onGoingEventArrayList.size() > 0) {
                setDataInOngoingEventAdapter();
            }

            if (upComingEventArrayList != null && upComingEventArrayList.size() > 0) {
                setDataInUpcomingEventAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInOngoingEventAdapter() {
        try {
            if (onGoingEventArrayList != null && onGoingEventArrayList.size() > 0) {
                fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_selected_round_button);
                fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_round_button);
//                fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_round_button);

                fragment_recyclerview_events.setVisibility(View.VISIBLE);
                fragment_page_indicator_events.setVisibility(View.VISIBLE);

              //  final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);
               // final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
                fragment_recyclerview_events.setLayoutManager(layoutManager);
                DashboardEventsListAdapter adapter = new DashboardEventsListAdapter(activity, onGoingEventArrayList);
                fragment_recyclerview_events.setAdapter(adapter);
                fragment_recyclerview_events.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();

              /*  fragment_page_indicator_events.setVisibility(View.VISIBLE);
                fragment_page_indicator_events.attachToRecyclerView(fragment_recyclerview_events, 0);
                fragment_page_indicator_events.setDotCount(onGoingEventArrayList.size());
                fragment_page_indicator_events.setCurrentPosition(0);
                fragment_page_indicator_events.setDotColor(Color.parseColor("#B6B6B6"));
                fragment_page_indicator_events.setSelectedDotColor(Color.parseColor("#ff0000"));
                fragment_page_indicator_events.setVisibleDotCount(7);
                fragment_recyclerview_events.setOnFlingListener(new RecyclerView.OnFlingListener() {

                    @Override
                    public boolean onFling(int velocityX, int velocityY) {

                        Log.e(TAG, "onScrolled: " + velocityX + "  " + velocityY);
                        SnapHelperOneByOne snapHelperOneByOne = new SnapHelperOneByOne();

                        if (velocityX != 0) {
                            int pos = snapHelperOneByOne.findTargetSnapPosition(gridLayoutManager, velocityX, velocityY);
                            Log.e(TAG, "onScrolled: " + pos);
                            if (pos >= 0) {
                                fragment_recyclerview_events.smoothScrollToPosition(pos);
                            }
                        }

                        return false;
                    }
                });*/
            }else{
                fragment_recyclerview_events.setVisibility(View.GONE);
                fragment_page_indicator_events.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInUpcomingEventAdapter() {
        try {
            if (upComingEventArrayList != null && upComingEventArrayList.size() > 0) {
                fragment_LL_ongoing_events.setBackgroundResource(R.drawable.order_top_round_button);
                fragment_LL_upcoming_events.setBackgroundResource(R.drawable.order_top_selected_round_button);
//                fragment_LL_post_requirement.setBackgroundResource(R.drawable.order_top_round_button);

                fragment_recyclerview_events.setVisibility(View.VISIBLE);
                fragment_page_indicator_events.setVisibility(View.VISIBLE);

               // final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);
               // final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
                fragment_recyclerview_events.setLayoutManager(layoutManager);
                DashboardEventsListAdapter adapter = new DashboardEventsListAdapter(activity, upComingEventArrayList);
                fragment_recyclerview_events.setAdapter(adapter);
                fragment_recyclerview_events.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();

             /*   fragment_page_indicator_events.setVisibility(View.VISIBLE);
                fragment_page_indicator_events.attachToRecyclerView(fragment_recyclerview_events, 0);
                fragment_page_indicator_events.setDotCount(upComingEventArrayList.size());
                fragment_page_indicator_events.setCurrentPosition(0);
                fragment_page_indicator_events.setDotColor(Color.parseColor("#B6B6B6"));
                fragment_page_indicator_events.setSelectedDotColor(Color.parseColor("#ff0000"));
                fragment_page_indicator_events.setVisibleDotCount(7);
                fragment_recyclerview_events.setOnFlingListener(new RecyclerView.OnFlingListener() {

                    @Override
                    public boolean onFling(int velocityX, int velocityY) {

                        Log.e(TAG, "onScrolled: " + velocityX + "  " + velocityY);
                        SnapHelperOneByOne snapHelperOneByOne = new SnapHelperOneByOne();

                        if (velocityX != 0) {
                            int pos = snapHelperOneByOne.findTargetSnapPosition(gridLayoutManager, velocityX, velocityY);
                            Log.e(TAG, "onScrolled: " + pos);
                            if (pos >= 0) {
                                fragment_recyclerview_events.smoothScrollToPosition(pos);
                            }
                        }

                        return false;
                    }
                });
*/            }else{
                fragment_recyclerview_events.setVisibility(View.GONE);
                fragment_page_indicator_events.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SnapHelperOneByOne extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {

            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                return RecyclerView.NO_POSITION;
            }

            final View currentView = findSnapView(layoutManager);
            if (currentView == null) {
                return RecyclerView.NO_POSITION;
            }

            final int currentPosition = layoutManager.getPosition(currentView);
            return currentPosition;
        }
    }

    public void setDataCardsForStats(String claimed_quantity, String confirmed_quantity, String pay_fail,
                                     String open_orders, String invested_amount, String pending_amount) {
        try {
            fragment_textview_claim_quantity.setText(claimed_quantity);
            fragment_textview_confirm_quantity.setText(confirmed_quantity);
            fragment_textview_failed_quantity.setText(pay_fail);

            fragment_textview_open_orders.setText(open_orders);
            fragment_textview_invested_amount.setText(activity.getString(R.string.rupees) + " " + invested_amount);
            fragment_textview_pending_amount.setText(activity.getString(R.string.rupees) + " " + pending_amount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}