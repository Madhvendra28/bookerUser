
package com.mdModel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayFailDatum {


    public PayFailDatum(Integer payFailModelId, String requirementModelId, String modelName, List<VariantDatum> variantData) {
        this.payFailModelId = payFailModelId;
        this.requirementModelId = requirementModelId;
        this.modelName = modelName;
        this.variantData = variantData;
    }

    private Integer payFailModelId;

    private String requirementModelId;

    private String modelName;

    private List<VariantDatum> variantData = null;

    public Integer getPayFailModelId() {
        return payFailModelId;
    }

    public void setPayFailModelId(Integer payFailModelId) {
        this.payFailModelId = payFailModelId;
    }

    public String getRequirementModelId() {
        return requirementModelId;
    }

    public void setRequirementModelId(String requirementModelId) {
        this.requirementModelId = requirementModelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<VariantDatum> getVariantData() {
        return variantData;
    }

    public void setVariantData(List<VariantDatum> variantData) {
        this.variantData = variantData;
    }

}
