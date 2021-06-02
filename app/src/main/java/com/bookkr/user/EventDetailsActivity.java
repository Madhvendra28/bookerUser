package com.bookkr.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ModelVariantAdapter;
import com.adapter.RequirementAddSiteNameRecyclerAdapter;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.model.Event;
import com.model.ModalVariant;
import com.model.Site;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.GetServerData;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.EventVariantSiteLinkDialog;
import com.ImagePreviewDialog;
import com.adapter.SliderAdapterVH;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.model.Event;
import com.model.EventModelVarient;
import com.model.ModalVariant;
import com.model.RequestParameter;
import com.model.Site;
import com.ortiz.touchview.TouchImageView;
import com.preferences.ShPrefUserDetails;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    //    private ImageView event_details_imageview_event;
    // private RecyclerView event_details_recyclerview_variants;
    private RecyclerView modelVarientList;
    public static final int PERMISSION_WRITE = 0;

    private TextView tvEventIDData, event_details_textview_event_name, event_details_textview_site_name, event_details_textview_sale_type,
            event_details_textview_sale_start, event_details_textview_sale_start_time, event_details_textview_sale_end, event_details_textview_sale_end_time, event_details_textview_modal_name,
            event_details_textview_bookkr_charge, event_details_textview_offer_title, event_details_textview_offer_details,
            event_details_textview_event_link, tvCreatedDate, tvUpdatedDate, tvEventNote;
    TextView tvOfferTitle, tvBookerCharge, tvEventLink, tvOfferDetail;
    LinearLayout llOfferTitle, llBookerCharge, llEventLink, llOfferDetail, llCreatedDate, llUpdatedDate, llEventNote;
    RecyclerView requirement_add_recyclerview_site_name;
    ImageView imgCopy;

    private ProgressDialog progress;
    private final String TAG = "RequirementDetails";
    private int requestFor = -1;

    private boolean requirementClaimed = false;
    private Event event;
    ImageView imgShare;

    Button event_details_button_claim;

    String event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            //event_details_imageview_event = findViewById(R.id.event_details_imageview_event);
            modelVarientList = findViewById(R.id.modelVarientList);
            // event_details_recyclerview_variants = findViewById(R.id.event_details_recyclerview_variants);

            tvEventIDData = findViewById(R.id.tvEventIDData);
            event_details_textview_event_name = findViewById(R.id.tvEventName);
            requirement_add_recyclerview_site_name = findViewById(R.id.recyclerSites);
            event_details_textview_sale_type = findViewById(R.id.event_details_textview_sale_type);
            event_details_textview_sale_start = findViewById(R.id.event_details_textview_sale_start);
            event_details_textview_sale_end = findViewById(R.id.event_details_textview_sale_end);
            // event_details_textview_modal_name = findViewById(R.id.event_details_textview_modal_name);
            event_details_textview_bookkr_charge = findViewById(R.id.event_details_textview_bookkr_charge);
            event_details_textview_offer_title = findViewById(R.id.event_details_textview_offer_title);
            event_details_textview_offer_details = findViewById(R.id.event_details_textview_offer_details);
            event_details_textview_event_link = findViewById(R.id.event_details_textview_event_link);
            tvOfferTitle = findViewById(R.id.tvOfferTitle);
            tvOfferDetail = findViewById(R.id.tvOfferDetail);
            tvBookerCharge = findViewById(R.id.tvBookerCharge);
            tvEventLink = findViewById(R.id.tvEventLink);
            llOfferTitle = findViewById(R.id.llOfferTitle);
            llOfferDetail = findViewById(R.id.llOfferDetail);
            llBookerCharge = findViewById(R.id.llBookerCharge);

            llCreatedDate = findViewById(R.id.llCreatedDate);
            llUpdatedDate = findViewById(R.id.llUpdatedDate);
            llEventNote = findViewById(R.id.llEventNote);
            llEventLink = findViewById(R.id.llEventLink);
            tvCreatedDate = findViewById(R.id.tvCreatedDate);
            tvUpdatedDate = findViewById(R.id.tvUpdatedDate);
            tvEventNote = findViewById(R.id.tvEventNote);
            imgCopy = findViewById(R.id.imgCopy);
            imgShare = findViewById(R.id.imgShare);

            event_details_button_claim = findViewById(R.id.btnShowRequirement);
            event_details_textview_sale_start_time = findViewById(R.id.event_details_textview_sale_start_time);
            event_details_textview_sale_end_time = findViewById(R.id.event_details_textview_sale_end_time);


            //event = HomeActivity.getActivity().getSelectedEvent();
            if (event == null) {
                try {
                    Intent intent = getIntent();
                    event_id = intent.getStringExtra(AppURLParams.event_id);
                    if (event_id !=null)
                        getEventDetails(event_id);
                    // Toast.makeText(this, "event : " + event_id, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                return;*/
            } else {
                setDataInViews();
            }

            imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.copyToClipboard(EventDetailsActivity.this, event_details_textview_event_link.getText().toString().trim());
                    Toast.makeText(EventDetailsActivity.this, "Link Copied!", Toast.LENGTH_SHORT).show();
                }
            });
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (event.getEvent_image() != null) {
                            if (checkPermission()) {
                                shareImage(event.getEvent_image().get(0), event.getShare_date());
                            }
                        } else {
                            shareImage("", event.getShare_date());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getEventDetails(String event_id) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            if (event != null) {
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getEventPreviewUser() + "/" + event_id);
            try {
                Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
                final LinkedHashMap<String, String> params = parameter.getParams();
                final String token = ShPrefUserDetails.getToken(this);
                StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri(), new Response.Listener<String>() {
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
                                    JSONObject jsonArray = !jsonObject.isNull(AppURLParams.data) ? jsonObject.getJSONObject(AppURLParams.data) : null;
                                    if (jsonArray != null) {
                                        event = JSONParser.parseEventPreview(jsonArray);
                                        if (event != null) {
                                            setDataInViews();
                                        } else {
                                            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                                            return;
                                        }
                                        //if (randomName == null || randomSurname == null || randomNameCode == null || randomShopTag == null) {
                                        //getAddressList();
                                        // getEventsList();
                                        //}
                                    } else {
                                        Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                                        return;
                                    }
                                } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                    Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();
                            Log.d("errorSome", e.toString());
                            return;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
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
                if (progress != null) {
                    progress.dismiss();
                }
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            Log.d("err", e.toString());
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (event != null) {
                setOfferBannerImages();
                tvEventIDData.setText("Event# " + event.getEvent_id_data());
                event_details_textview_event_name.setText(" : " + event.getEvent_name() + "");
                event_details_textview_sale_type.setText(event.getSale_type() + "");
//                event_details_textview_modal_name.setText(event.getModel_name() + "");
                event_details_textview_sale_start.setText(AppUtils.parseDateTodMMMEEEForEventDetails(event.getStart_date()) + "");
                event_details_textview_sale_start_time.setText(AppUtils.parseDateTohmmaForEventDetails(event.getStart_date()) + "");
                event_details_textview_sale_end.setText(AppUtils.parseDateTodMMMEEEForEventDetails(event.getEnd_date()) + "");
                event_details_textview_sale_end_time.setText(AppUtils.parseDateTohmmaForEventDetails(event.getEnd_date()) + "");
                Log.d("event", event.toString());
                event_details_button_claim.setText("View Requirement (" + event.getTotal_requirement() + ")");

                if (!event.getBookkr_charges().isEmpty()) {
                    event_details_textview_bookkr_charge.setText(getString(R.string.rupees) + " " + event.getBookkr_charges());
                    tvBookerCharge.setVisibility(View.VISIBLE);
                    event_details_textview_bookkr_charge.setVisibility(View.VISIBLE);
                    llBookerCharge.setVisibility(View.VISIBLE);

                } else {
                    tvBookerCharge.setVisibility(View.GONE);
                    event_details_textview_bookkr_charge.setVisibility(View.GONE);
                    llBookerCharge.setVisibility(View.GONE);
                }

                if (!event.getOffer_title().isEmpty()) {
                    event_details_textview_offer_title.setText(event.getOffer_title() + "");
                    tvOfferTitle.setVisibility(View.VISIBLE);
                    event_details_textview_offer_title.setVisibility(View.VISIBLE);
                    llOfferTitle.setVisibility(View.VISIBLE);

                } else {
                    tvOfferTitle.setVisibility(View.GONE);
                    event_details_textview_offer_title.setVisibility(View.GONE);
                    llOfferTitle.setVisibility(View.GONE);
                }

                if (!event.getOffer_details().isEmpty()) {
                    event_details_textview_offer_details.setText(event.getOffer_details() + "");
                    AppUtils.makeTextViewResizable(event_details_textview_offer_details, 10, "Read more", true);
                    tvOfferDetail.setVisibility(View.VISIBLE);
                    event_details_textview_offer_details.setVisibility(View.VISIBLE);
                    llOfferDetail.setVisibility(View.VISIBLE);
                } else {
                    tvOfferDetail.setVisibility(View.GONE);
                    event_details_textview_offer_details.setVisibility(View.GONE);
                    llOfferDetail.setVisibility(View.GONE);
                }
                /*changed*/
                if (!event.getEvent_note().isEmpty()) {
                    tvEventNote.setText(event.getEvent_note() + "");
                    llEventNote.setVisibility(View.VISIBLE);
                } else {
                    llEventNote.setVisibility(View.GONE);
                }

                if (!event.getCreate_date().isEmpty()) {
                    tvCreatedDate.setText(event.getCreate_date() + "");
                    llCreatedDate.setVisibility(View.VISIBLE);
                } else {
                    llCreatedDate.setVisibility(View.GONE);
                }
                if (!event.getUpdate_date().isEmpty()) {
                    tvUpdatedDate.setText(event.getUpdate_date() + "");
                    llUpdatedDate.setVisibility(View.VISIBLE);
                } else {
                    llUpdatedDate.setVisibility(View.GONE);
                }
                /*changed*/

                //event_details_textview_event_link.setText(event.getEvent_link() + "");
                if (!event.getEvent_link().isEmpty()) {
                    //  event_details_textview_event_link.setText(" " + Html.fromHtml(event.getEvent_link()));

                    Spannable span = Spannable.Factory.getInstance().newSpannable(event.getEvent_link());
                    span.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(event.getEvent_link()));
                                startActivity(i);
                            } catch (Exception e) {
                                Toast.makeText(EventDetailsActivity.this, "Unable to open Site", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, 0, event.getEvent_link().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    event_details_textview_event_link.setText(span);
                    event_details_textview_event_link.setMovementMethod(LinkMovementMethod.getInstance());
                    event_details_textview_event_link.setVisibility(View.VISIBLE);
                    llEventLink.setVisibility(View.VISIBLE);
                } else {
                    tvEventLink.setVisibility(View.GONE);
                    event_details_textview_event_link.setVisibility(View.GONE);
                    llEventLink.setVisibility(View.GONE);
                }

                ArrayList<EventModelVarient> modalVariantArrayList = event.getEventModelVarients();
                if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                    modelVarientList.setVisibility(View.VISIBLE);
                    EventModelVariantAdapter adapter = new EventModelVariantAdapter(modalVariantArrayList);
                    modelVarientList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    modelVarientList.setAdapter(adapter);
                } else {
                    modelVarientList.setVisibility(View.GONE);
                }
                /*ArrayList<Site> sites = event.getSites();
                String s = "";
                for(int i =0;i< sites.size();i++){
                    s += sites.get(i).getSite_name();

                    if (i !=sites.size() - 1) {
                        s += ", ";
                    }
                }
                event_details_textview_site_name.setText(""+s + "");*/

              /*  String s = "";
                for (int i = 0; i < event.getSite_name().size(); i++) {
                    s += event.getSite_name().get(i);

                    if (i != event.getSite_name().size() - 1) {
                        s += ", ";
                    }
                }*/

               /* ArrayList<ModalVariant> modalVariantArrayList = event.getEventModelVarients().get(0).getModalVariantArrayList();
                if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                    event_details_recyclerview_variants.setVisibility(View.VISIBLE);
//                    event_details_recyclerview_variants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//                    PostReqModalVariantRecyclerAdapter adapter = new PostReqModalVariantRecyclerAdapter(this, modalVariantArrayList);

                    ModelVariantAdapter adapter = new ModelVariantAdapter(this, event.getEventModelVarients().get(0).getModel_variant());
                    event_details_recyclerview_variants.setLayoutManager(new GridLayoutManager(this, 2));
                    event_details_recyclerview_variants.setAdapter(adapter);

                } else {
                    event_details_recyclerview_variants.setVisibility(View.GONE);
                }
*/
                ArrayList<Site> list = event.getSites();
                requirement_add_recyclerview_site_name.setVisibility(View.VISIBLE);
                requirement_add_recyclerview_site_name.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                SiteImageAdaptor adapter = new SiteImageAdaptor(list);
                requirement_add_recyclerview_site_name.setAdapter(adapter);

                try {
                    if (!event.getEvent_image().equals("")) {
                    /*    ImageRequest imageRequest = new ImageRequest(event.getEvent_image(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                           *//* event_details_imageview_event.setVisibility(View.VISIBLE);
                                            event_details_imageview_event.setImageBitmap(bitmap);*//*
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                AppURLParams.imageWidthMerchant, AppURLParams.imageHeightMerchant, null, null);
                        GetServerData.addRequestToQueue(this, imageRequest);*/
                    } else {
                        //event_details_imageview_event.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    //  event_details_imageview_event.setVisibility(View.GONE);
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

    /*public void postRequirement(View view) {
        try {
            Intent intent = new Intent(this, RequirementAddActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppURLParams.event_id, event.getEvent_id() + "");
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/

    @Override
    public void onBackPressed() {
        event = null;
        if (requirementClaimed) {
            setResult(301);
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

  /*  public void btnReport(View view) {
        if (event != null) {
            EventReportDialog dialog = new EventReportDialog(this, event);
            dialog.show();
        } else {
            giveErrorOnError();
        }
    }*/

    public void btnViewRequirement(View view) {
        startActivity(new Intent(EventDetailsActivity.this, RequirementListActivity.class).putExtra(AppURLParams.event_id, event_id));
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        /*startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://wa.me/918818820007")));*/
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
           /* Glide.with(holder.itemView)
                    .load(sites.get(position).getSite_image())
                    .fitCenter()
                    .into(holder.imgSiteImage);*/
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

    private void setOfferBannerImages() {
        final SliderView sliderView = findViewById(R.id.eventImageSlider);
        TouchImageView eventImage1 = findViewById(R.id.eventImage1);
        try {
            if (event != null) {
                if (event.getEvent_image() != null) {
                    if (event.getEvent_image().size() > 1) {
                        EventImageSliderAdapter adapter = new EventImageSliderAdapter(this, event.getEvent_image());
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();
                        eventImage1.setVisibility(View.GONE);
                        sliderView.setVisibility(View.VISIBLE);
                    } else if (event.getEvent_image().size() == 1) {
                        sliderView.setVisibility(View.GONE);
                        eventImage1.setVisibility(View.VISIBLE);
                        Picasso.get().load(event.getEvent_image().get(0)).into(eventImage1);

                        eventImage1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new ImagePreviewDialog(EventDetailsActivity.this, event.getEvent_image().get(0)).show();
                            }
                        });
                    } else {
                        sliderView.setVisibility(View.GONE);
                        eventImage1.setVisibility(View.GONE);
                    }

                } else {
                    sliderView.setVisibility(View.GONE);
                    eventImage1.setVisibility(View.GONE);
                }

            } else {
                sliderView.setVisibility(View.GONE);
                eventImage1.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sliderView.setVisibility(View.GONE);
            eventImage1.setVisibility(View.GONE);
        }
    }

    public void shareImage(String url, final String shareData) {
        final String[] fileUri = new String[1];
        if (!TextUtils.isEmpty(url)) {
            Picasso.get().load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                        if (!mydir.exists()) {
                            mydir.mkdirs();
                        }
                        fileUri[0] = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                        FileOutputStream outputStream = new FileOutputStream(fileUri[0]);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri[0]), null, null));
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,/*"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +*/shareData);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "Share with"));

//                    Intent share = new Intent(Intent.ACTION_SEND);
//                    share.setType("*/*");   // share.setType("image/*");
//                    share.putExtra(Intent.EXTRA_STREAM, uri);
//                    share.putExtra(Intent.EXTRA_TEXT, shareData);
//                    activity.startActivity(Intent.createChooser(share, "Share Store"));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

        } else {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT,/*"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +*/shareData);
            shareIntent.setType("text/*");
            startActivity(Intent.createChooser(shareIntent, "Share with"));
        }
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    class EventImageSliderAdapter extends SliderViewAdapter<SliderAdapterVH> {

        private Context context;
        private ArrayList<String> mSliderItems = new ArrayList<>();

        public EventImageSliderAdapter(Context context, ArrayList<String> mSliderItems) {
            this.context = context;
            this.mSliderItems = mSliderItems;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_offer_banner_image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
            try {
                // final OfferBanner offerBanner = mSliderItems.get(position);
                Picasso.get().load(mSliderItems.get(position).replace("https", "http")).into(viewHolder.imageViewBackground);
                viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImagePreviewDialog(EventDetailsActivity.this, mSliderItems.get(position)).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return mSliderItems.size();
        }
    }

    class EventModelVariantAdapter extends RecyclerView.Adapter<EventModelVariantAdapter.ViewHolder> {
        ArrayList<EventModelVarient> eventModelVariants;

        public EventModelVariantAdapter(ArrayList<EventModelVarient> eventModelVariants) {
            this.eventModelVariants = eventModelVariants;
        }

        @NonNull
        @Override
        public EventModelVariantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EventDetailsActivity.this).inflate(R.layout.layout_model_varient, parent, false);
            return new EventModelVariantAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvModelName.setText(eventModelVariants.get(position).getModel_name());
            if (!eventModelVariants.get(position).getModel_note().isEmpty()) {
                holder.tvModelNote.setText(eventModelVariants.get(position).getModel_note());
                holder.llEventVarientNote.setVisibility(View.VISIBLE);
            } else {
                holder.llEventVarientNote.setVisibility(View.GONE);
            }
            holder.modelVarientList.setLayoutManager(new LinearLayoutManager(EventDetailsActivity.this, RecyclerView.VERTICAL, false));
            holder.modelVarientList.setAdapter(new EventVariantAdapter(eventModelVariants.get(position).getModalVariantArrayList()));
        }

        @Override
        public int getItemCount() {
            return eventModelVariants.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvModelName, tvModelNote;
            RecyclerView modelVarientList;
            LinearLayout llEventVarientNote;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvModelName = itemView.findViewById(R.id.tvModelName);
                modelVarientList = itemView.findViewById(R.id.modelVarientList);
                tvModelNote = itemView.findViewById(R.id.tvModelNote);
                llEventVarientNote = itemView.findViewById(R.id.llEventVarientNote);
            }
        }
    }

    class EventVariantAdapter extends RecyclerView.Adapter<EventVariantAdapter.ViewHolder> {
        ArrayList<ModalVariant> models;

        public EventVariantAdapter(ArrayList<ModalVariant> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public EventVariantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EventDetailsActivity.this).inflate(R.layout.layout_event_variant, parent, false);
            return new EventVariantAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvVariantName.setText(models.get(position).getVariant_name());
            holder.tvPrice.setText(getString(R.string.rupees) + models.get(position).getVariant_price());

            Spannable span = Spannable.Factory.getInstance().newSpannable("Link \uD83D\uDD17");
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    EventVariantSiteLinkDialog dialog = new EventVariantSiteLinkDialog(EventDetailsActivity.this, models.get(position).getVariantSites());
                    dialog.show();
                    //  Log.d("main", "link clicked");
                    //  Toast.makeText(EventDetailActivity.this, "link clicked", Toast.LENGTH_SHORT).show();
                }
            }, 0, "Link \uD83D\uDD17".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tvView.setText(span);

            holder.tvView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvVariantName, tvPrice, tvView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvVariantName = itemView.findViewById(R.id.tvVariantName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvView = itemView.findViewById(R.id.tvView);
            }
        }
    }
}
