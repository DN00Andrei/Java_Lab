package org.example.repository;

import org.example.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

    @Query("SELECT r FROM ResultEntity r WHERE r.jucator.id = :jucatorId")
    List<ResultEntity> gasesteRezultateJucator(@Param("jucatorId") Long jucatorId);

    @Query("SELECT r FROM ResultEntity r WHERE r.joc.id = :jocId ORDER BY r.puncteObtinute DESC")
    List<ResultEntity> gasesteRezultateJoc(@Param("jocId") Long jocId);

    @Query("SELECT SUM(r.puncteObtinute) FROM ResultEntity r WHERE r.jucator.id = :jucatorId")
    Integer totalPuncteJucator(@Param("jucatorId") Long jucatorId);
}
