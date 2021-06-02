package com.model;

public class Site {
    String site_name;
    String site_image;

    public Site() {
    }

    public Site(String site_name, String site_image) {
        this.site_name = site_name;
        this.site_image = site_image;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getSite_image() {
        return site_image;
    }

    public void setSite_image(String site_image) {
        this.site_image = site_image;
    }

    @Override
    public String toString() {
        return "Site{" +
                "site_name='" + site_name + '\'' +
                ", site_image='" + site_image + '\'' +
                '}';
    }
}
