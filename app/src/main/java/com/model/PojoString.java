package com.model;

public class PojoString {
    String stringName;
    boolean isChecked;

    public PojoString(String stringName) {
        this.stringName = stringName;
    }

    public PojoString(String stringName, boolean isChecked) {
        this.stringName = stringName;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }
}