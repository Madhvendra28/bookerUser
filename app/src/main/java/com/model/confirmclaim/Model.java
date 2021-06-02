
package com.model.confirmclaim;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Model {

    @SerializedName("requirement_model_id")
    @Expose
    private Integer requirementModelId;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("variant")
    @Expose
    private List<Variant> variant = null;

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
