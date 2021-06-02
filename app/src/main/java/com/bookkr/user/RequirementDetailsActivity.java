package com.bookkr.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.EventVariantSiteLinkDialog;
import com.adapter.PosrReqTextLinkRecyclerAdapter;
import com.adapter.RequirementAddSiteNameRecyclerAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.model.Address;
import com.model.ModelPusAddress;
import com.model.ModelPusStore;
import com.model.PojoModel;
import com.model.PojoVariant;
import com.model.PostRequirement;
import com.model.RequestParameter;
import com.model.Site;
import com.model.SitePrecautionTemplate;
import com.model.TextLink;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequirementDetailsActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerModelVariants, recyclerAddress, recyclerPusAddress, recyclerLinks, recyclerSites, recyclerPrecautions;
    private TextView tvEventName, tvEventIDData, tvStartDate, tvEndDate, tvStartTime, tvEndTime, tvSaleType, tvSelectPrecautions, edtDealerNote, tvAddressTitle;

    private ProgressDialog progress;
    private final String TAG = "RequirementDetails";
    private int requestFor = -1;

    private boolean requirementClaimed = false;
    private PostRequirement postRequirement;
    RequirementDetailsActivity activity;
    private LinearLayout nodata_LL;
    private TextView nodata_textview;
    String postRequirementID = "";
    TextView tvRequirementStatus;
    ImageView imgResumePause;

    Menu menuHeader;
    private LinearLayout
            layoutAddresses, llDealerNote;
    TextView tvCompensationForRTO, tvPaymentMode, tvRequiredQuantity, tvClaimedQuantity, tvDealerCanPay, tvCreatedDate, tvUpdatedDate, tvBookkrCharges, tvForThisRequirement;

    boolean isDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_details);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity = this;
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            recyclerModelVariants = findViewById(R.id.recyclerModelVariants);
            recyclerAddress = findViewById(R.id.recyclerAddress);
            recyclerPusAddress = findViewById(R.id.recyclerPusAddress);
            recyclerLinks = findViewById(R.id.recyclerLinks);
            recyclerSites = findViewById(R.id.recyclerSites);
            nodata_LL = findViewById(R.id.nodata_LL);
            nodata_textview = findViewById(R.id.nodata_textview);
            tvEventName = findViewById(R.id.tvEventName);
            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);
            tvStartTime = findViewById(R.id.tvStartTime);
            tvEndTime = findViewById(R.id.tvEndTime);
            tvSaleType = findViewById(R.id.tvSaleType);
            tvRequiredQuantity = findViewById(R.id.tvRequiredQuantity);
            tvClaimedQuantity = findViewById(R.id.tvClaimedQuantity);
            tvDealerCanPay = findViewById(R.id.tvDealerCanPay);
            tvCompensationForRTO = findViewById(R.id.tvCompensationForRTO);
            tvPaymentMode = findViewById(R.id.tvPaymentMode);
            tvAddressTitle = findViewById(R.id.tvAddressTitle);
            tvSelectPrecautions = findViewById(R.id.tvSelectPrecautions);
            edtDealerNote = findViewById(R.id.edtDealerNote);
            llDealerNote = findViewById(R.id.llDealerNote);
            tvCreatedDate = findViewById(R.id.tvCreatedDate);
            tvUpdatedDate = findViewById(R.id.tvUpdatedDate);
            tvBookkrCharges = findViewById(R.id.tvBookkrCharges);
            tvForThisRequirement = findViewById(R.id.tvForThisRequirement);

            tvRequirementStatus = findViewById(R.id.tvRequirementStatus);
            imgResumePause = findViewById(R.id.imgResumePause);

            layoutAddresses = findViewById(R.id.layoutAddresses);
            recyclerPrecautions = findViewById(R.id.recyclerPrecautions);
            tvEventIDData = findViewById(R.id.tvEventIDData);

            postRequirementID = getIntent().getStringExtra("requirement_id");
            Log.d("serajdata",AppURL.getAppURL() + AppURL.getPostReqPreviewUser() + "/" + postRequirementID);

            if (postRequirement == null) {
                getRequirementDetails();
            } else {
                setDataInViews();
            }

        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    class SitePrecautionAdapter extends RecyclerView.Adapter<SitePrecautionAdapter.ViewHolder> {
        List<SitePrecautionTemplate> sitePrecautionTemplates;

        public SitePrecautionAdapter(List<SitePrecautionTemplate> sitePrecautionTemplates) {
            this.sitePrecautionTemplates = sitePrecautionTemplates;
        }

        @NonNull
        @Override
        public SitePrecautionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SitePrecautionAdapter.ViewHolder(LayoutInflater.from(RequirementDetailsActivity.this).inflate(R.layout.layout_precaution, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final SitePrecautionAdapter.ViewHolder holder, int position) {
            holder.tvPrecautionSiteName.setText(sitePrecautionTemplates.get(position).getSite_name());
            holder.edtPrecaution.setText(sitePrecautionTemplates.get(position).getDescription());
            holder.edtPrecaution.setEnabled(false);
        }

        @Override
        public int getItemCount() {
            return sitePrecautionTemplates == null ? 0 : sitePrecautionTemplates.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvPrecautionSiteName;
            EditText edtPrecaution;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                edtPrecaution = itemView.findViewById(R.id.edtPrecaution);
                tvPrecautionSiteName = itemView.findViewById(R.id.tvPrecautionSiteName);
            }
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

    private void setDataInViews() {
        try {
            if (postRequirement != null) {
                ArrayList<Site> list1 = postRequirement.getSites();
                SiteImageAdaptor adapter1 = new SiteImageAdaptor(list1);
                recyclerSites.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                recyclerSites.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                getSupportActionBar().setTitle("Req#" + postRequirement.getRequirement_id() + " Details");
                tvEventName.setText(postRequirement.getEvent_name() + "");
                if (postRequirement.getEvent_id().equalsIgnoreCase("0")) {
                    tvEventName.setVisibility(View.GONE);
                    tvEventIDData.setVisibility(View.GONE);
                }
                String eventIdData = "Event# " + postRequirement.getEvent_id_data() + "";
                Spannable span = Spannable.Factory.getInstance().newSpannable(eventIdData);
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(activity, EventDetailsActivity.class);
                            intent.putExtra(AppURLParams.event_id, postRequirement.getEvent_id());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                            /*String url = textLinkArrayList.get(position).getPost_link();
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);*/
                        } catch (Exception e) {
                            //Toast.makeText(RequirementDetailsActivity.this, "Unable to open Site", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, 0, eventIdData.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvEventIDData.setText(span);
                tvEventIDData.setMovementMethod(LinkMovementMethod.getInstance());

                tvSaleType.setText(postRequirement.getSale_type() + "");
                tvRequiredQuantity.setText("Required\nQty :" + postRequirement.getRequired_quantity() + "");
                tvClaimedQuantity.setText("Claimed\nQty :" + postRequirement.getClaim_quantity() + "");
//                tvPaymentOn.setText(postRequirement.getPayment_on() + "");
                tvDealerCanPay.setText("Dealer Can\nPay : #" + postRequirement.getCan_pay() + "");
                // tvPrecautions.setText(postRequirement.getPrecautions() + "");
                if (!TextUtils.isEmpty(postRequirement.getNote())) {
                    llDealerNote.setVisibility(View.VISIBLE);
                    edtDealerNote.setText(postRequirement.getNote() + "");
                } else {
                    llDealerNote.setVisibility(View.GONE);
                }
                tvPaymentMode.setText(postRequirement.getPayment_on());
                tvCreatedDate.setText(postRequirement.getPosted_date());
                tvUpdatedDate.setText(postRequirement.getUpdated_date());
                tvBookkrCharges.setText(getString(R.string.rupees) + " " + postRequirement.getBookkr_charges());
                tvForThisRequirement.setText(getString(R.string.rupees) + " " + postRequirement.getTotal_bookker_charge());
                if (postRequirement.getIs_closed().equalsIgnoreCase("1")) {
                    imgResumePause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                    tvRequirementStatus.setText("Resumed");
                } else {
                    imgResumePause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_resume));
                    tvRequirementStatus.setText("Paused");
                }

               /* if (postRequirement.getPayment_on().equalsIgnoreCase(getString(R.string.shipping))) {
                    rbtnShoping.setChecked(true);
                } else if (postRequirement.getPayment_on().equalsIgnoreCase(getString(R.string.same_day))) {
                    rbtnSameDay.setChecked(true);
                } else if (postRequirement.getPayment_on().equalsIgnoreCase(getString(R.string.within_ndays))) {
                    rbtnWithinNDays.setChecked(true);
                }*/

                if (postRequirement.getRto().equals(AppURLParams.statusVal1)) {
                    tvCompensationForRTO.setText("Yes (" + getString(R.string.rupees) + " " + postRequirement.getRto_charges() + ")");
                    /*rbtnCompensationYes.setChecked(true);
                    edtCompensationAmount.setText(postRequirement.getRto_charges() + "");*/
                } else {
                    tvCompensationForRTO.setText("No");
                    /*rbtnCompensationNo.setChecked(true);
                    edtCompensationAmount.setText("");*/
                }

                tvStartDate.setText(AppUtils.parseDateTodMMMEEEForEventDetails(postRequirement.getStart_date()) + "");
                tvStartTime.setText(AppUtils.parseDateTohmmaForEventDetails(postRequirement.getStart_date()) + "");
                tvEndDate.setText(AppUtils.parseDateTodMMMEEEForEventDetails(postRequirement.getEnd_date()) + "");
                tvEndTime.setText(AppUtils.parseDateTohmmaForEventDetails(postRequirement.getEnd_date()) + "");


                List<PojoModel> models = postRequirement.getModelVariantList();
                ModelVariantAdapter modelVariantAdapter = new ModelVariantAdapter(models);
                recyclerModelVariants.setLayoutManager(new LinearLayoutManager(activity));
                recyclerModelVariants.setAdapter(modelVariantAdapter);
                int addressSize = 0;

                List<Address> addressArrayList = postRequirement.getAddressList();
                if (addressArrayList != null && addressArrayList.size() > 0) {
                    addressSize += addressArrayList.size();
                    recyclerAddress.setVisibility(View.VISIBLE);
                    recyclerAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    AddressListRecyclerAdapter adapter = new AddressListRecyclerAdapter(this, addressArrayList);
                    recyclerAddress.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerAddress.setVisibility(View.GONE);
                }

                List<ModelPusAddress> pusAddressList = postRequirement.getPusAddressList();
                if (pusAddressList != null && pusAddressList.size() > 0) {
                    addressSize += pusAddressList.size();
                    recyclerPusAddress.setVisibility(View.VISIBLE);
                    recyclerPusAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    AddressPusListRecyclerAdapter adapter = new AddressPusListRecyclerAdapter(this, pusAddressList);
                    recyclerPusAddress.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerPusAddress.setVisibility(View.GONE);
                }

                tvAddressTitle.setText("Address : " + addressSize);
                ArrayList<TextLink> textLinkArrayList = postRequirement.getTextLinkArrayList();

                layoutAddresses.setVisibility(addressSize > 0 ? View.VISIBLE : View.GONE);

                if (textLinkArrayList != null && textLinkArrayList.size() > 0) {
                    recyclerLinks.setVisibility(View.VISIBLE);
                    recyclerLinks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    TextAndLinkAdapter adapter = new TextAndLinkAdapter(textLinkArrayList);
                    recyclerLinks.setAdapter(adapter);
                } else {
                    recyclerLinks.setVisibility(View.GONE);
                }

                JSONArray precautionArray = new JSONArray(postRequirement.getPrecautions());
                List<SitePrecautionTemplate> sitePrecautionTemplates = new ArrayList<>();
                for (int i = 0; i < precautionArray.length(); i++) {
                    JSONObject precautionObject = precautionArray.getJSONObject(i);
                    SitePrecautionTemplate precautionTemplate = new SitePrecautionTemplate();
                    precautionTemplate.setSite_name(precautionObject.getString("site_name"));
                    precautionTemplate.setDescription(precautionObject.getString("description"));
                    sitePrecautionTemplates.add(precautionTemplate);
                }

                if (sitePrecautionTemplates.size() > 0) {
                    String selectedPrecautions = "";
                    for (int i = 0; i < sitePrecautionTemplates.size(); i++) {
                        selectedPrecautions += sitePrecautionTemplates.get(i).getSite_name() + ",";
                    }
                    tvSelectPrecautions.setText(selectedPrecautions);
                    SitePrecautionAdapter adaptor = new SitePrecautionAdapter(sitePrecautionTemplates);
                    recyclerPrecautions.setLayoutManager(new LinearLayoutManager(RequirementDetailsActivity.this));
                    recyclerPrecautions.setHasFixedSize(true);
                    recyclerPrecautions.setAdapter(adaptor);
                } else {
                    tvSelectPrecautions.setText("No precautions text available");
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

    @Override
    public void onBackPressed() {
        postRequirement = null;
        if (isDeleted) {
            setResult(111);
        }
        /*postRequirement = null;
        if (requirementClaimed) {
            setResult(301);
        }*/
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == 111) {
                getRequirementDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TextAndLinkAdapter extends RecyclerView.Adapter<TextAndLinkAdapter.ViewHolder> {
        ArrayList<TextLink> textLinkArrayList;

        public TextAndLinkAdapter(ArrayList<TextLink> textLinkArrayList) {
            this.textLinkArrayList = textLinkArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TextAndLinkAdapter.ViewHolder(LayoutInflater.from(RequirementDetailsActivity.this).inflate(R.layout.layout_requirement_preview_link, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvText.setText(textLinkArrayList.get(position).getText() + " : ");

            Spannable span = Spannable.Factory.getInstance().newSpannable(textLinkArrayList.get(position).getPost_link());
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    try {
                        String url = textLinkArrayList.get(position).getPost_link();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(RequirementDetailsActivity.this, "Unable to open Site", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, 0, textLinkArrayList.get(position).getPost_link().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tvLink.setText(span);
            holder.tvLink.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public int getItemCount() {
            return textLinkArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvText, tvLink;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvText = itemView.findViewById(R.id.tvText);
                tvLink = itemView.findViewById(R.id.tvLink);
            }
        }
    }

    private void getRequirementDetails() {
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

            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            Log.d("serajdata",AppURL.getAppURL() + AppURL.getPostReqPreviewUser() + "/" + postRequirementID);
            parameter.setUri(AppURL.getAppURL() + AppURL.getPostReqPreviewUser() + "/" + postRequirementID);
            getDataFromServer(parameter);
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    class ModelVariantAdapter extends RecyclerView.Adapter<ModelVariantAdapter.ViewHolder> {
        List<PojoModel> models;

        public ModelVariantAdapter(List<PojoModel> models) {
            ModelVariantAdapter.this.models = models;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ModelVariantAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_preview_model_varients, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvSelectModal.setText(ModelVariantAdapter.this.models.get(position).getModel_name());
            holder.recyclerVariants.setLayoutManager(new LinearLayoutManager(activity));
            holder.recyclerVariants.setAdapter(new VariantAdapter(ModelVariantAdapter.this.models.get(position).getVariants(), ModelVariantAdapter.this.models.get(position).getModel_name()));
        }

        @Override
        public int getItemCount() {
            return ModelVariantAdapter.this.models.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSelectModal;//, tvSelectVariant;
            RecyclerView recyclerVariants;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSelectModal = itemView.findViewById(R.id.tvSelectModal);
              //  tvSelectVariant = itemView.findViewById(R.id.tvSelectVariant);
                recyclerVariants = itemView.findViewById(R.id.recyclerVariants);
            }
        }

        class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {
            List<PojoVariant> models;
            String model_name;

            public VariantAdapter(List<PojoVariant> models, String model_name) {
                VariantAdapter.this.models = models;
                VariantAdapter.this.model_name = model_name;
            }

            @NonNull
            @Override
            public VariantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new VariantAdapter.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_preview_variant, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull VariantAdapter.ViewHolder holder, final int position) {

                holder.tvVariantName.setText(model_name + ", " + VariantAdapter.this.models.get(position).getVariant_name() + "");
                // holder.tvVariantName.setText(VariantAdapter.this.models.get(position).getVariant_name());
                holder.tvVarientPrice.setText(getResources().getString(R.string.rupees) + VariantAdapter.this.models.get(position).getVariant_price());
                holder.tvSelectColor.setText(VariantAdapter.this.models.get(position).getVariant_color());
                holder.tvRateType.setText(VariantAdapter.this.models.get(position).getRate_type());
                holder.edtCod.setText(VariantAdapter.this.models.get(position).getCod_price());
                holder.edtPrePaid.setText(VariantAdapter.this.models.get(position).getPrepaid_price());
                holder.edtPayFail.setText(VariantAdapter.this.models.get(position).getPayfail_price());
                holder.edtOtpVerified.setText(VariantAdapter.this.models.get(position).getOtp_verify());
                Spannable span = Spannable.Factory.getInstance().newSpannable("Link \uD83D\uDD17");
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View v) {
                        EventVariantSiteLinkDialog dialog = new EventVariantSiteLinkDialog(RequirementDetailsActivity.this, VariantAdapter.this.models.get(position).getVariantSites());
                        dialog.show();
                    }
                }, 0, "Link \uD83D\uDD17".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvView.setText(span);
                holder.tvView.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public int getItemCount() {
                return VariantAdapter.this.models.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView tvVariantName, tvVarientPrice, tvSelectColor, tvRateType, tvView;
                EditText edtCod, edtPrePaid, edtPayFail, edtOtpVerified;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    tvVariantName = itemView.findViewById(R.id.tvVariantName);
                    tvVarientPrice = itemView.findViewById(R.id.tvVarientPrice);
                    tvSelectColor = itemView.findViewById(R.id.tvSelectColor);
                    tvRateType = itemView.findViewById(R.id.tvRateType);
                    edtCod = itemView.findViewById(R.id.edtCod);
                    edtPrePaid = itemView.findViewById(R.id.edtPrePaid);
                    edtPayFail = itemView.findViewById(R.id.edtPayFail);
                    edtOtpVerified = itemView.findViewById(R.id.edtOtpVerified);
                    tvView = itemView.findViewById(R.id.tvView);
                }
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_preview, menu);
        menuHeader = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_requirement_share:
                try {
                    Intent sendIntent1 = new Intent();
                    sendIntent1.setAction(Intent.ACTION_SEND);
                    sendIntent1.putExtra(Intent.EXTRA_TEXT, postRequirement.getShare_data());
                    sendIntent1.setType("text/plain");
                    activity.startActivity(sendIntent1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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
            if (requestFor == 1) {

                JSONObject jsonArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONObject(AppURLParams.data) : null;
                postRequirement = JSONParser.parsePostRequirement(jsonArray);

                if (postRequirement != null) {
                    nodata_LL.setVisibility(View.GONE);
                    setDataInViews();
                } else {
                    postRequirement = null;
                    giveErrorOnNoData();
                }
            } else if (requestFor == 2) {

                if (postRequirement.getIs_closed().equalsIgnoreCase("1")) {
                    postRequirement.setIs_closed("0");
                    tvRequirementStatus.setText("Paused");
                    imgResumePause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_resume));
                } else {
                    postRequirement.setIs_closed("1");
                    tvRequirementStatus.setText("Resumed");
                    imgResumePause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pause));
                }
            } else if (requestFor == 3) {
                //deleted
                deletedSuccessFully();
            }
        } catch (Exception e) {
            postRequirement = null;
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void deletedSuccessFully() {
        isDeleted = true;
        onBackPressed();
    }


    public void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
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
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_try_later));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AddressListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Activity activity;
        private List<Address> list;

        public AddressListRecyclerAdapter(Activity activity, List<Address> siteData) {
            this.activity = activity;
            this.list = siteData;
        }

        @NonNull
        @Override
        public AddressListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.adapter_address_item_layout, parent, false);
            return new AddressListRecyclerAdapter.MyViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            try {
                ((AddressListRecyclerAdapter.MyViewHolder) holder).bindAddressListItems(AddressListRecyclerAdapter.this, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout adapter_LL_container, adapter_LL_operation;
            private TextView adapter_textview_website, adapter_textview_payment_mode, adapter_textview_name, adapter_textview_house_number,
                    adapter_textview_shop_tags, adapter_textview_colony, adapter_textview_area, adapter_textview_landmark, adapter_textview_pincode,
                    adapter_textview_city, adapter_textview_state, adapter_textview_note_for_site, adapter_textview_auto_fill_link,
                    adapter_textview_address, adapter_textview_contact_no;
            ImageView adapter_imageview_edit, adapter_imageview_delete, adapter_imageview_share;
            MaterialButton btnGenerateAddress;
            CheckBox chkSelect;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                adapter_textview_website = itemView.findViewById(R.id.adapter_textview_website);
                adapter_textview_payment_mode = itemView.findViewById(R.id.adapter_textview_payment_mode);
                adapter_textview_name = itemView.findViewById(R.id.adapter_textview_name);
                adapter_textview_house_number = itemView.findViewById(R.id.adapter_textview_house_number);
                adapter_textview_shop_tags = itemView.findViewById(R.id.adapter_textview_shop_tags);
                adapter_textview_contact_no = itemView.findViewById(R.id.adapter_textview_contact_no);
                adapter_textview_address = itemView.findViewById(R.id.adapter_textview_address);
                adapter_textview_colony = itemView.findViewById(R.id.adapter_textview_colony);
                adapter_textview_area = itemView.findViewById(R.id.adapter_textview_area);
                adapter_textview_landmark = itemView.findViewById(R.id.adapter_textview_landmark);
                adapter_textview_pincode = itemView.findViewById(R.id.adapter_textview_pincode);
                adapter_textview_city = itemView.findViewById(R.id.adapter_textview_city);
                adapter_textview_state = itemView.findViewById(R.id.adapter_textview_state);
                adapter_textview_note_for_site = itemView.findViewById(R.id.adapter_textview_note_for_site);
                adapter_textview_auto_fill_link = itemView.findViewById(R.id.adapter_textview_auto_fill_link);

                adapter_LL_operation = itemView.findViewById(R.id.adapter_LL_operation);
                btnGenerateAddress = itemView.findViewById(R.id.btnGenerateAddress);
                chkSelect = itemView.findViewById(R.id.chkSelect);
                adapter_imageview_delete = itemView.findViewById(R.id.adapter_imageview_delete);
                adapter_imageview_edit = itemView.findViewById(R.id.adapter_imageview_edit);
                adapter_imageview_share = itemView.findViewById(R.id.adapter_imageview_share);

            }

            private void bindAddressListItems(AddressListRecyclerAdapter adapter, final int position) {
                try {
                    final Address address = list.get(position);
                    adapter_textview_payment_mode.setText(address.getPayment_option() + "");
                    adapter_textview_name.setText(address.getName() + " " + address.getSurname() + ", " + address.getName_code());
                    adapter_textview_contact_no.setText(address.getContact_no() + "");
                    adapter_textview_address.setText(address.getAddress() + "");
                    adapter_textview_colony.setText(address.getAddressLists().get(0).getColony_name() + "");
                    adapter_textview_area.setText(address.getAddressLists().get(0).getArea() + "");
                    adapter_textview_landmark.setText(address.getAddressLists().get(0).getLandmark() + "");
                    adapter_textview_pincode.setText(address.getAddressLists().get(0).getPostal_code() + "");
                    adapter_textview_city.setText(address.getCity() + "");
                    adapter_textview_state.setText(address.getState() + "");
                    adapter_textview_note_for_site.setText(address.getNote().trim() + "");
                    adapter_textview_auto_fill_link.setText(address.getLink() + "");
                    btnGenerateAddress.setVisibility(View.VISIBLE);
                    adapter_LL_operation.setVisibility(View.VISIBLE);
                    adapter_imageview_edit.setVisibility(View.GONE);
                    adapter_imageview_delete.setVisibility(View.GONE);
                    btnGenerateAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent intent = new Intent(activity, GenrateAddressActivity.class);
                            intent.putExtra("address_id", address.getAddress_id());
                            activity.startActivity(intent);*/
                            // activity.startActivityForResult(intent, 330);
                        }
                    });
                    adapter_imageview_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent sendIntent1 = new Intent();
                                sendIntent1.setAction(Intent.ACTION_SEND);
                                sendIntent1.putExtra(Intent.EXTRA_TEXT, address.getShare_data());
                                sendIntent1.setType("text/plain");
                                activity.startActivity(sendIntent1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    String s = address.getHouse_name().replace("||", " / ");
                    adapter_textview_house_number.setText(s + "");

                    s = address.getShop_tag();
                    if (s.equalsIgnoreCase(AppURLParams.statusVal1)) {
                        adapter_textview_shop_tags.setText(activity.getString(R.string.yes));
                    } else {
                        adapter_textview_shop_tags.setText(activity.getString(R.string.no));
                    }
                    adapter_textview_website.setText(address.getSite_name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class AddressPusListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Activity activity;
        private List<ModelPusAddress> list;

        public AddressPusListRecyclerAdapter(Activity activity, List<ModelPusAddress> siteData) {
            this.activity = activity;
            this.list = siteData;
        }

        @NonNull
        @Override
        public AddressPusListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.adapter_address_pus_item_layout, parent, false);
            return new AddressPusListRecyclerAdapter.MyViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            try {
                ((AddressPusListRecyclerAdapter.MyViewHolder) holder).bindAddressListItems(AddressPusListRecyclerAdapter.this, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout adapter_LL_container, adapter_LL_operation;
            private TextView adapter_textview_website, adapter_textview_payment_mode, adapter_textview_name, adapter_textview_contact_no;
            private RecyclerView recyclerPusStoreList;
            private CheckBox chkSelect;
            ImageView adapter_imageview_share, adapter_imageview_edit, adapter_imageview_delete;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                adapter_textview_website = itemView.findViewById(R.id.adapter_textview_website);
                adapter_textview_payment_mode = itemView.findViewById(R.id.adapter_textview_payment_mode);
                adapter_textview_name = itemView.findViewById(R.id.adapter_textview_name);
                adapter_textview_contact_no = itemView.findViewById(R.id.adapter_textview_contact_no);

                adapter_LL_operation = itemView.findViewById(R.id.adapter_LL_operation);
                recyclerPusStoreList = itemView.findViewById(R.id.recyclerPusStoreList);
                chkSelect = itemView.findViewById(R.id.chkSelect);
                adapter_imageview_share = itemView.findViewById(R.id.adapter_imageview_share);
                adapter_imageview_delete = itemView.findViewById(R.id.adapter_imageview_delete);
                adapter_imageview_edit = itemView.findViewById(R.id.adapter_imageview_edit);
            }

            private void bindAddressListItems(AddressPusListRecyclerAdapter adapter, final int position) {
                try {
                    final ModelPusAddress address = list.get(position);
                    adapter_textview_payment_mode.setText(address.getPayment_option() + "");
                    adapter_textview_name.setText(address.getName() + " " + address.getSurname() + ", " + address.getName_code());
                    adapter_textview_contact_no.setText(address.getContact_no() + "");
                    recyclerPusStoreList.setLayoutManager(new LinearLayoutManager(activity));
                    recyclerPusStoreList.setAdapter(new StoreListAdapter(address.getPus_address()));
                    adapter_LL_operation.setVisibility(View.VISIBLE);
                    chkSelect.setChecked(address.isChecked());
                    adapter_textview_website.setText(address.getSite_name());
                    adapter_imageview_edit.setVisibility(View.GONE);
                    adapter_imageview_delete.setVisibility(View.GONE);
                    adapter_imageview_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent sendIntent1 = new Intent();
                                sendIntent1.setAction(Intent.ACTION_SEND);
                                sendIntent1.putExtra(Intent.EXTRA_TEXT, address.getShare_data());
                                sendIntent1.setType("text/plain");
                                activity.startActivity(sendIntent1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
            List<ModelPusStore> modelPusStores;

            public StoreListAdapter(List<ModelPusStore> modelPusStores) {
                this.modelPusStores = modelPusStores;
            }

            @NonNull
            @Override
            public StoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(activity).inflate(R.layout.layout_store_list_item, parent, false);
                return new StoreListAdapter.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull final StoreListAdapter.ViewHolder holder, final int position) {
                holder.tvStoreName.setText("" + modelPusStores.get(position).getStore_name());
                holder.tvPincode.setText("" + modelPusStores.get(position).getPostal_code());
                holder.tvAddress.setText("" + modelPusStores.get(position).getStore_address());
                holder.imgShare.setVisibility(View.GONE);
                try {
                    Picasso.get().load(modelPusStores.get(position).getFull_store_image()).into(holder.imgStoreImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.imgStoreImage.setVisibility(View.GONE);
                }
            }

            @Override
            public int getItemCount() {
                return modelPusStores.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView tvStoreName, tvPincode, tvAddress;
                ImageView imgStoreImage, imgShare;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    tvStoreName = itemView.findViewById(R.id.tvStoreName);
                    tvPincode = itemView.findViewById(R.id.tvPincode);
                    tvAddress = itemView.findViewById(R.id.tvAddress);
                    imgStoreImage = itemView.findViewById(R.id.imgStoreImage);
                    imgShare = itemView.findViewById(R.id.imgShare);
                }
            }
        }
    }
}
