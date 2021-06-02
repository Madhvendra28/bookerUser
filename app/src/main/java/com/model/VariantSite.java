package com.model;

import java.util.ArrayList;

public class VariantSite {
    private String site_name;
    private ArrayList<VariantSiteLink> links;

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public ArrayList<VariantSiteLink> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<VariantSiteLink> links) {
        this.links = links;
    }
}
