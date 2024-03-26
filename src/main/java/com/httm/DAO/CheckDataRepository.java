package com.httm.DAO;

import com.httm.Model.CheckData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckDataRepository extends JpaRepository<CheckData, Long> {
    @Query("SELECT c.soluong FROM CheckData c WHERE c.id = 1")
    Long findSoluong();
    @Modifying
    @Query("UPDATE CheckData cd SET cd.soluong = :soluong WHERE cd.id=1")
    void updateSoluong(@Param("soluong") Long soluong);
}
