package com.httm.DAO;

import com.httm.Model.Data;
import com.httm.Model.MachineLearningModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
    @Query("SELECT COUNT(*) FROM Data")
    Long countData();

    @Query("select d from Data d where d.IdImage between :id1 and :id2")
    List<Data> findAllByIdImageBetween(Long id1, Long id2);

    @Query("SELECT d FROM Data d WHERE d.Label = :label")
    List<Data> findAllByLabel(@Param("label") String label);
}
