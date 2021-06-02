package com.bookkr.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fragment.RequirementClaimDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.model.PostRequirement;
import com.model.RequestParameter;
import com.model.Site;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RequirementListActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView requirement_list_recyclerview_items;
    private LinearLayout requirement_list_LL_all, requirement_list_LL_flash_sale, requirement_list_LL_open_sale, requirement_list_LL_hide_n_seek;
    private LinearLayout requirement_list_LL_container, nodata_LL;
    private TextView nodata_textview;

    private static RequirementListActivity activity;
    private int requestFor = -1;
    private ProgressDialog progress;
    private final String TAG = "Requirement List";

    private String event_id;
    private List<PostRequirement> postRequirementArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_list);
        final String token = ShPrefUserDetails.getToken(this);
        Log.e("token",token);


        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            requirement_list_recyclerview_items = findViewById(R.id.requirement_list_recyclerview_items);
            requirement_list_LL_container = findViewById(R.id.requirement_list_LL_container);

            requirement_list_LL_all = findViewById(R.id.requirement_list_LL_all);
            requirement_list_LL_flash_sale = findViewById(R.id.requirement_list_LL_flash_sale);
            requirement_list_LL_open_sale = findViewById(R.id.requirement_list_LL_open_sale);
            requirement_list_LL_hide_n_seek = findViewById(R.id.requirement_list_LL_hide_n_seek);

            nodata_LL = findViewById(R.id.nodata_LL);
            nodata_textview = findViewById(R.id.nodata_textview);

            activity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                event_id = getIntent().getExtras().getString(AppURLParams.event_id);
            }

            if (postRequirementArrayList == null) {
                getRequirementList();
            } else {
                setDataInAllRequirementAdapter();
            }

            requirement_list_LL_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDataInAllRequirementAdapter();
                }
            });

            requirement_list_LL_flash_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDataInFlashRequirementAdapter();
                }
            });

            requirement_list_LL_open_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDataInOpenRequirementAdapter();
                }
            });

            requirement_list_LL_hide_n_seek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDataInHidenSeekRequirementAdapter();
                }
            });
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getRequirementList() {
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

            requestFor = 1;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getDealerPostReqList());
            if (event_id != null) {
                parameter.setParam(AppURLParams.event_id, event_id);
            } else {
                parameter.setParam(AppURLParams.event_id, "");
            }

            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri() + "?" + parameter.getEncodedParams(),
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
                                            postRequirementArrayList = JSONParser.parsePostRequirementList(jsonArray);
                                            if (postRequirementArrayList != null) {
                                                setDataInAllRequirementAdapter();

                                            } else {
                                                postRequirementArrayList = null;
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

    private void setDataInAllRequirementAdapter() {
        try {
            requirement_list_LL_all.setBackgroundResource(R.drawable.order_top_selected_round_button);
            requirement_list_LL_flash_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_open_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_hide_n_seek.setBackgroundResource(R.drawable.order_top_round_button);

            if (postRequirementArrayList != null && postRequirementArrayList.size() > 0) {
                requirement_list_LL_container.setVisibility(View.VISIBLE);
                requirement_list_recyclerview_items.setVisibility(View.VISIBLE);
                nodata_LL.setVisibility(View.GONE);

                RequirementRecyclerAdapter adapter = new RequirementRecyclerAdapter(this, postRequirementArrayList);
                requirement_list_recyclerview_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requirement_list_recyclerview_items.getContext(), LinearLayoutManager.VERTICAL);
                requirement_list_recyclerview_items.addItemDecoration(mDividerItemDecoration);
                requirement_list_recyclerview_items.setAdapter(adapter);
                requirement_list_recyclerview_items.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();

            } else {
                requirement_list_LL_container.setVisibility(View.GONE);
                nodata_LL.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInFlashRequirementAdapter() {
        try {
            requirement_list_LL_all.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_flash_sale.setBackgroundResource(R.drawable.order_top_selected_round_button);
            requirement_list_LL_open_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_hide_n_seek.setBackgroundResource(R.drawable.order_top_round_button);

            if (postRequirementArrayList != null && postRequirementArrayList.size() > 0) {
                ArrayList<PostRequirement> list = new ArrayList<>();
                for (int i = 0; i < postRequirementArrayList.size(); i++) {
                    PostRequirement postRequirement = postRequirementArrayList.get(i);
                    if (postRequirement.getSale_type().equalsIgnoreCase(getString(R.string.flash_sale))) {
                        list.add(postRequirement);
                    }
                }

                if (list != null && list.size() > 0) {
                    requirement_list_LL_container.setVisibility(View.VISIBLE);
                    requirement_list_recyclerview_items.setVisibility(View.VISIBLE);
                    nodata_LL.setVisibility(View.GONE);

                    RequirementRecyclerAdapter adapter = new RequirementRecyclerAdapter(this, list);
                    requirement_list_recyclerview_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requirement_list_recyclerview_items.getContext(), LinearLayoutManager.VERTICAL);
                    requirement_list_recyclerview_items.addItemDecoration(mDividerItemDecoration);
                    requirement_list_recyclerview_items.setAdapter(adapter);
                    requirement_list_recyclerview_items.setNestedScrollingEnabled(false);
                    adapter.notifyDataSetChanged();
                } else {
                    requirement_list_recyclerview_items.setVisibility(View.GONE);
                    nodata_LL.setVisibility(View.VISIBLE);
                }

            } else {
                requirement_list_LL_container.setVisibility(View.GONE);
                nodata_LL.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInOpenRequirementAdapter() {
        try {
            requirement_list_LL_all.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_flash_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_open_sale.setBackgroundResource(R.drawable.order_top_selected_round_button);
            requirement_list_LL_hide_n_seek.setBackgroundResource(R.drawable.order_top_round_button);

            if (postRequirementArrayList != null && postRequirementArrayList.size() > 0) {
                ArrayList<PostRequirement> list = new ArrayList<>();
                for (int i = 0; i < postRequirementArrayList.size(); i++) {
                    PostRequirement postRequirement = postRequirementArrayList.get(i);
                    if (postRequirement.getSale_type().equalsIgnoreCase(getString(R.string.open_sale))) {
                        list.add(postRequirement);
                    }
                }

                if (list != null && list.size() > 0) {
                    requirement_list_LL_container.setVisibility(View.VISIBLE);
                    requirement_list_recyclerview_items.setVisibility(View.VISIBLE);
                    nodata_LL.setVisibility(View.GONE);

                    RequirementRecyclerAdapter adapter = new RequirementRecyclerAdapter(this, list);
                    requirement_list_recyclerview_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requirement_list_recyclerview_items.getContext(), LinearLayoutManager.VERTICAL);
                    requirement_list_recyclerview_items.addItemDecoration(mDividerItemDecoration);
                    requirement_list_recyclerview_items.setAdapter(adapter);
                    requirement_list_recyclerview_items.setNestedScrollingEnabled(false);
                    adapter.notifyDataSetChanged();
                } else {
                    requirement_list_recyclerview_items.setVisibility(View.GONE);
                    nodata_LL.setVisibility(View.VISIBLE);
                }

            } else {
                requirement_list_LL_container.setVisibility(View.GONE);
                nodata_LL.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInHidenSeekRequirementAdapter() {
        try {
            requirement_list_LL_all.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_flash_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_open_sale.setBackgroundResource(R.drawable.order_top_round_button);
            requirement_list_LL_hide_n_seek.setBackgroundResource(R.drawable.order_top_selected_round_button);

            if (postRequirementArrayList != null && postRequirementArrayList.size() > 0) {
                ArrayList<PostRequirement> list = new ArrayList<>();
                for (int i = 0; i < postRequirementArrayList.size(); i++) {
                    PostRequirement postRequirement = postRequirementArrayList.get(i);
                    if (postRequirement.getSale_type().equalsIgnoreCase(getString(R.string.hide_n_seek))) {
                        list.add(postRequirement);
                    }
                }

                if (list != null && list.size() > 0) {
                    requirement_list_LL_container.setVisibility(View.VISIBLE);
                    requirement_list_recyclerview_items.setVisibility(View.VISIBLE);
                    nodata_LL.setVisibility(View.GONE);

                    RequirementRecyclerAdapter adapter = new RequirementRecyclerAdapter(this, list);
                    requirement_list_recyclerview_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(requirement_list_recyclerview_items.getContext(), LinearLayoutManager.VERTICAL);
                    requirement_list_recyclerview_items.addItemDecoration(mDividerItemDecoration);
                    requirement_list_recyclerview_items.setAdapter(adapter);
                    requirement_list_recyclerview_items.setNestedScrollingEnabled(false);
                    adapter.notifyDataSetChanged();
                } else {
                    requirement_list_recyclerview_items.setVisibility(View.GONE);
                    nodata_LL.setVisibility(View.VISIBLE);
                }

            } else {
                requirement_list_LL_container.setVisibility(View.GONE);
                nodata_LL.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        getRequirementList();
    }

    public static RequirementListActivity getActivity() {
        return activity;
    }

    public PostRequirement getPostRequirementAtPosition(int position) {
        if (postRequirementArrayList != null) {
            return postRequirementArrayList.get(position);
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        setResult(31);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_req_list, menu);
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

        } else if (id == R.id.action_req_history) {
            redirectUserHistory();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void redirectUserHistory() {
        try {
            Intent intent = new Intent(this, UserClaimHistoryListActivity.class);
            startActivityForResult(intent, 302);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            requirement_list_LL_container.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_no_data_found));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void giveErrorOnError() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            requirement_list_LL_container.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RequirementRecyclerAdapter extends RecyclerView.Adapter<RequirementRecyclerAdapter.ViewHolder> {
        private static final String TAG = "PostReqRecyclerAdapter";
        private List<PostRequirement> list;
        private Context context;

        public RequirementRecyclerAdapter(Context context, List<PostRequirement> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RequirementRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_req_item_layout, parent, false);
            return new RequirementRecyclerAdapter.ViewHolder(view);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final RequirementRecyclerAdapter.ViewHolder holder, final int position) {
            try {
                final PostRequirement postRequirement = list.get(position);

                holder.tvProductName.setText(postRequirement.getModel_name());
                holder.tvDate.setText(AppUtils.convertDateWithoutTime(postRequirement.getStart_date()) + "");
                holder.tvStartTime.setText(AppUtils.getFormattedDateTime(postRequirement.getStart_timing()) + "");
                holder.tvSaleType.setText(postRequirement.getSale_type() + "");
                holder.tvClaimed.setText(postRequirement.getClaim_quantity() + "");
                holder.tvTotalQuantity.setText(postRequirement.getRequired_quantity() + "");
                holder.tvDealerName.setText(postRequirement.getDealer_name() + "");
                holder.recyclerSite.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
                ArrayList<Site> sites = postRequirement.getSites();
                holder.recyclerSite.setAdapter(new SiteImageAdaptor(sites));
                try {
                    int claimedQty = Integer.parseInt(postRequirement.getClaim_quantity());
                    int reqQty = Integer.parseInt(postRequirement.getRequired_quantity());

                    if (reqQty > claimedQty) {
                        holder.adapter_textview_claim_available_tag.setVisibility(View.VISIBLE);
                        holder.btnClaimNow.setVisibility(View.VISIBLE);
                    } else {
                        holder.adapter_textview_claim_available_tag.setVisibility(View.GONE);
                        holder.btnClaimNow.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(activity, RequirementDetailsActivity.class);
                            intent.putExtra("requirement_id", postRequirement.getRequirement_id());
                            activity.startActivityForResult(intent,111);
                            activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.btnClaimNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Spconstant.isForClaimorUpdate =true;
                        RequirementClaimDialogFragment claimDialogFragment = new RequirementClaimDialogFragment(
                                postRequirement.getRequirement_id(), postRequirement.getEvent_id(),
                                postRequirement.getRequired_quantity(), postRequirement.getClaim_quantity(), AppURLParams.statusVal1);
                        claimDialogFragment.show(((RequirementListActivity) context).getSupportFragmentManager(), AppURLParams.claimDialogFragment);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imgReqImage;
            private TextView tvClaimed, tvTotalQuantity, adapter_textview_claim_available_tag,
                    tvProductName, tvDate, tvStartTime, tvSaleType,
                    tvDealerName;
            private Button btnClaimNow;
            RecyclerView recyclerSite;

            public ViewHolder(View itemView) {
                super(itemView);
                imgReqImage = itemView.findViewById(R.id.imgReqImage);
//            adapter_imageview_action_star = itemView.findViewById(R.id.adapter_imageview_action_star);

                tvClaimed = itemView.findViewById(R.id.tvClaimed);
                tvTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity);
                adapter_textview_claim_available_tag = itemView.findViewById(R.id.adapter_textview_claim_available_tag);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvStartTime = itemView.findViewById(R.id.tvStartTime);
                tvSaleType = itemView.findViewById(R.id.tvSaleType);
                tvDealerName = itemView.findViewById(R.id.tvDealerName);
                recyclerSite = itemView.findViewById(R.id.recyclerSite);
//            adapter_textview_varient = itemView.findViewById(R.id.adapter_textview_varient);
//            adapter_textview_shop_name = itemView.findViewById(R.id.adapter_textview_shop_name);adapter_textview_rating = itemView.findViewById(R.id.adapter_textview_rating);
                btnClaimNow = itemView.findViewById(R.id.btnClaimNow);

            }
        }

        class SiteImageAdaptor extends RecyclerView.Adapter<SiteImageAdaptor.ViewHolder> {
            ArrayList<Site> sites;

            public SiteImageAdaptor(ArrayList<Site> sites) {
                this.sites = sites;
            }

            @NonNull
            @Override
            public SiteImageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_site_image, parent, false);
                return new SiteImageAdaptor.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull SiteImageAdaptor.ViewHolder holder, int position) {
                Picasso.get().load(sites.get(position).getSite_image().replace("https", "http")).into(holder.imgSiteImage);
            }

            @Override
            public int getItemCount() {
                return sites.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                ImageView imgSiteImage;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    imgSiteImage = itemView.findViewById(R.id.imgSiteImage);
                }
            }
        }
    }
}
