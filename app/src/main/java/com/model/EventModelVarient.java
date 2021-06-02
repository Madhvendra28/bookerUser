package com.model;

import java.util.ArrayList;

public class EventModelVarient {
    private String model_name;
    private String model_id;
    private String model_note;
    private ArrayList<String> model_variant;
    private ArrayList<ModalVariant> modalVariantArrayList;
    private ArrayList<String> colorList;

    public String getModel_name() {
        return model_name;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public ArrayList<String> getModel_variant() {
        return model_variant;
    }

    public String getModel_note() {
        return model_note;
    }

    public void setModel_note(String model_note) {
        this.model_note = model_note;
    }

    public void setModel_variant(ArrayList<String> model_variant) {
        this.model_variant = model_variant;
    }

    public ArrayList<ModalVariant> getModalVariantArrayList() {
        return modalVariantArrayList;
    }

    public void setModalVariantArrayList(ArrayList<ModalVariant> modalVariantArrayList) {
        this.modalVariantArrayList = modalVariantArrayList;
    }
}
