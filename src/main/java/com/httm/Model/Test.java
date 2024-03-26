package com.httm.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "test_data")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_Image")
    private int IdImage;
    @Column(name = "Link_path")
    private String Link_Path;
    @Column(name = "Feature")
    private String Feature;
    @Column(name = "Label")
    private String Label;

    public Test() {
    }

    public Test(int idImage, String link_Path, String feature, String label) {
        IdImage = idImage;
        Link_Path = link_Path;
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
        return Link_Path;
    }

    public void setLink_path(String link_path) {
        Link_Path = link_path;
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
        return "Test{" +
                "IdImage=" + IdImage +
                ", Link_Path='" + Link_Path + '\'' +
                ", Feature='" + Feature + '\'' +
                ", Label='" + Label + '\'' +
                '}';
    }
}
