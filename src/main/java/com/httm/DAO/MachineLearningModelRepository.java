package com.httm.DAO;

import com.httm.Model.LearningHistoryModel;
import com.httm.Model.MachineLearningModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MachineLearningModelRepository extends JpaRepository<MachineLearningModel, String> {
    List<MachineLearningModel> findByTrainingBetween(Date fromDate, Date toDate);
    List<MachineLearningModel> findByIdModel(@Param("ID_Model") Integer idModel);
    @Query(value = "INSERT INTO machinelearningmodel (Name_Model, Training_Time, Model_Path, Accuracy) VALUES (:namemodel, :trainingtime, :modelpath, :accuracy)",
            nativeQuery = true)
    void insertMachineLearningModel(@Param("namemodel") String nameModel, @Param("trainingtime") Date trainingTime, @Param("modelpath") String modelPath, @Param("accuracy") double accuracy);

}
