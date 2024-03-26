package com.httm.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "machinelearningmodel")
public class MachineLearningModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_Model")
    private int idModel;
    @Column(name = "Name_Model")
    private String Name_Model;
    @Column(name = "Training_Time")
    private Date training;
    @Column(name = "Model_Path")
    private String Model_Path;
    @Column(name = "Accuracy")
    private double Accuracy;

    public MachineLearningModel() {
    }

    public MachineLearningModel(String name_Model, Date training_Time, String model_Path, double accuracy) {
        Name_Model = name_Model;
        training = training_Time;
        Model_Path = model_Path;
        Accuracy = accuracy;
    }

    public int getID_Model() {
        return idModel;
    }

    public void setID_Model(int ID_Model) {
        this.idModel = ID_Model;
    }

    public String getName_Model() {
        return Name_Model;
    }

    public void setName_Model(String name_Model) {
        Name_Model = name_Model;
    }

    public Date getTraining_Time() {
        return training;
    }

    public void setTraining_Time(Date training_Time) {
        training = training_Time;
    }

    public String getModel_Path() {
        return Model_Path;
    }

    public void setModel_Path(String model_Path) {
        Model_Path = model_Path;
    }

    public double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(double accuracy) {
        Accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "MachineLearningModel{" +
                "id_Model='" + idModel + '\'' +
                ", Name_Model='" + Name_Model + '\'' +
                ", Training_Time=" + training +
                ", Model_Path='" + Model_Path + '\'' +
                ", Accuracy='" + Accuracy + '\'' +
                '}';
    }

}
