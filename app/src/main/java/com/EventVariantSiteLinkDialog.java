package com;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkr.user.R;
import com.google.android.material.card.MaterialCardView;
import com.model.SiteData;
import com.model.VariantSite;
import com.model.VariantSiteLink;
import com.utils.AppURL;
import com.utils.AppUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventVariantSiteLinkDialog extends Dialog {
    private Context mContext;
    private ArrayList<VariantSite> variantSites;
    RecyclerView listSiteNames, listSiteLinks;
    ArrayList<SiteData> models;
    TextView tvNoDataAvailable;

    public EventVariantSiteLinkDialog(Context context, ArrayList<VariantSite> variantSites) {
        super(context);
        mContext = context;
        this.variantSites = variantSites;
        models = new ArrayList<>();
        for (int i = 0; i < variantSites.size(); i++) {
            SiteData siteData = new SiteData();
            siteData.setSite_name(variantSites.get(i).getSite_name());
            if (i == 0) {
                siteData.setChecked(true);
            }
            models.add(siteData);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_variant_site_links);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        listSiteNames = findViewById(R.id.listSiteNames);
        tvNoDataAvailable = findViewById(R.id.tvNoDataAvailable);

        if (models.size() > 0) {
            listSiteNames.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            listSiteLinks = findViewById(R.id.listSiteLinks);
            listSiteLinks.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            listSiteNames.setAdapter(new SiteNameAdapter(models));
            tvNoDataAvailable.setVisibility(View.GONE);
            listSiteLinks.setAdapter(new SiteLinkAdapter(variantSites.get(0).getLinks()));
        } else {
            tvNoDataAvailable.setVisibility(View.VISIBLE);
        }
    }

    class SiteNameAdapter extends RecyclerView.Adapter<SiteNameAdapter.ViewHolder> {
        ArrayList<SiteData> models;

        public SiteNameAdapter(ArrayList<SiteData> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_site_list_tab, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvSiteName.setText(models.get(position).getSite_name());
            if (models.get(position).isChecked()) {
                holder.cardSiteName.setStrokeColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.cardSiteName.setStrokeColor(Color.WHITE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < models.size(); i++) {
                        if (i != position) {
                            models.get(i).setChecked(false);
                        } else {
                            //siteFilter = models.get(position).getSite_name();
                            models.get(i).setChecked(true);
                        }
                    }
                    listSiteLinks.setAdapter(new SiteLinkAdapter(variantSites.get(position).getLinks()));
                    // setDataInAddressListAdapter();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSiteName;
            MaterialCardView cardSiteName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSiteName = itemView.findViewById(R.id.tvSiteName);
                cardSiteName = itemView.findViewById(R.id.cardSiteName);
            }
        }
    }

    class SiteLinkAdapter extends RecyclerView.Adapter<SiteLinkAdapter.ViewHolder> {
        ArrayList<VariantSiteLink> links;

        public SiteLinkAdapter(ArrayList<VariantSiteLink> links) {
            this.links = links;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_site_link, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvVariantColor.setText(links.get(position).getModel_color());
            Spannable span = Spannable.Factory.getInstance().newSpannable(links.get(position).getLink());
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(links.get(position).getLink()));
                        mContext.startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Unable to open page", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, 0, links.get(position).getLink().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tvVariantLink.setText(span);

            holder.tvVariantLink.setMovementMethod(LinkMovementMethod.getInstance());
            holder.imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.copyToClipboard(mContext, links.get(position).getLink());
                    Toast.makeText(mContext, "Link Copied!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return links.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvVariantColor, tvVariantLink;
            ImageView imgCopy;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvVariantColor = itemView.findViewById(R.id.tvVariantColor);
                tvVariantLink = itemView.findViewById(R.id.tvVariantLink);
                imgCopy = itemView.findViewById(R.id.imgCopy);
            }
        }
    }
}
