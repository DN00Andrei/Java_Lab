package org.example.repository;

import org.example.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

    @Query("SELECT o FROM OptionEntity o WHERE o.intrebare.id = :intrebareId")
    List<OptionEntity> gasesteOptiuniPentruIntrebare(@Param("intrebareId") Long intrebareId);
}
