package com.httm.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;

public class Modelinfo {
    @JsonProperty("Accuracy")
    private double Accuracy;
    @JsonProperty("Link_model")
    private String Link_model;
    @JsonProperty("Name_model")
    private String Name_model;

    public double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(double accuracy) {
        Accuracy = accuracy;
    }

    public String getLink_model() {
        return Link_model;
    }

    public void setLink_model(String link_model) {
        Link_model = link_model;
    }

    public String getName_model() {
        return Name_model;
    }

    public void setName_model(String name_model) {
        Name_model = name_model;
    }
}
