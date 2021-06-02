package com.model;


import java.util.ArrayList;

public class States {
    String stateName;
    ArrayList<String> cities;

    public States(String stateName, ArrayList<String> cities) {
        this.stateName = stateName;
        this.cities = cities;
    }

    public States() {
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }
}
