package com.model.confirmclaim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MdModel {


    private Integer requirementModelId;


    private String modelName;

    private List<Variant> variant = null;


    public MdModel(Integer requirementModelId, String modelName, List<Variant> variant) {
        this.requirementModelId = requirementModelId;
        this.modelName = modelName;
        this.variant = variant;
    }

    public Integer getRequirementModelId() {
        return requirementModelId;
    }

    public void setRequirementModelId(Integer requirementModelId) {
        this.requirementModelId = requirementModelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<Variant> getVariant() {
        return variant;
    }

    public void setVariant(List<Variant> variant) {
        this.variant = variant;
    }
}
