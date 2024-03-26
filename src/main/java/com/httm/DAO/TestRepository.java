package com.httm.DAO;

import com.httm.Model.Data;
import com.httm.Model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, String> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO test_data (Link_Path, Feature, Label) " +
            "SELECT Link_path, Feature, Label FROM data WHERE ID_Image BETWEEN :startId AND :endId",
            nativeQuery = true)
    void saveDataToTest(@Param("startId") Long startId, @Param("endId") Long endId);
    @Modifying
    @Query(value = "TRUNCATE TABLE test_data", nativeQuery = true)
    void truncateTable();
}
