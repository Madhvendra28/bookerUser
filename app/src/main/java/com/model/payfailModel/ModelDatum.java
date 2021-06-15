
package com.model.payfailModel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ModelDatum {

    @SerializedName("requirement_model_id")
    @Expose
    private Integer requirementModelId;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("variant_data")
    @Expose
    private List<VariantDatum> variantData = null;

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

    public List<VariantDatum> getVariantData() {
        return variantData;
    }

    public void setVariantData(List<VariantDatum> variantData) {
        this.variantData = variantData;
    }

}
