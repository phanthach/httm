package com.httm.DAO;

import com.httm.Model.LearningHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface LearningHistoryModelRepository extends JpaRepository<LearningHistoryModel, String> {
    List<LearningHistoryModel> findByTrainingBetween(Date fromDate, Date toDate);
    List<LearningHistoryModel> findByIdModel(@Param("ID_Model") Integer idModel);
    @Modifying
    @Query(value = "INSERT INTO learninghistorymodel (Name_Model, Training_Time, Model_Path, Accuracy) VALUES (:namemodel, :trainingtime, :modelpath, :accuracy)",
            nativeQuery = true)
    void insertLearningHistoryModel(@Param("namemodel") String nameModel, @Param("trainingtime") Date trainingTime, @Param("modelpath") String modelPath, @Param("accuracy") double accuracy);
}
