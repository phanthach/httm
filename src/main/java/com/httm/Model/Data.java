package com.httm.Model;

import jakarta.persistence.*;


@Entity
@lombok.Data
@Table(name = "data")
public class Data{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_Image")
    private int IdImage;
    @Column(name = "Link_path")
    private String Link_path;
    @Column(name = "Feature")
    private String Feature;
    @Column(name = "Label")
    private String Label;

    public Data() {
    }

    public Data(int idImage, String link_path, String feature, String label) {
        IdImage = idImage;
        Link_path = link_path;
        Feature = feature;
        Label = label;
    }

    public int getIdImage() {
        return IdImage;
    }

    public void setIdImage(int idImage) {
        IdImage = idImage;
    }

    public String getLink_path() {
        return Link_path;
    }

    public void setLink_path(String link_path) {
        Link_path = link_path;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @Override
    public String toString() {
        return "Data{" +
                "IdImage=" + IdImage +
                ", Link_path='" + Link_path + '\'' +
                ", Feature='" + Feature + '\'' +
                ", Label='" + Label + '\'' +
                '}';
    }
}
