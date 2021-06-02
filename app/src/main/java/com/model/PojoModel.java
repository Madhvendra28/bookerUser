package com.model;


import java.util.ArrayList;
import java.util.List;

public class PojoModel {
    private String model_name;
    private String model_id;
    private String requirement_model_id;
    private List<PojoVariant> variants = new ArrayList<>();
    private ArrayList<String> colorList;

    private List<PojoVariant> allVariants = new ArrayList<>();

    public String getModel_name() {
        return model_name;
    }

    public List<PojoVariant> getAllVariants() {
        return allVariants;
    }

    public void setAllVariants(List<PojoVariant> allVariants) {
        this.allVariants = allVariants;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public String getRequirement_model_id() {
        return requirement_model_id;
    }

    public void setRequirement_model_id(String requirement_model_id) {
        this.requirement_model_id = requirement_model_id;
    }

    public List<PojoVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<PojoVariant> variants) {
        this.variants = variants;
    }
}
