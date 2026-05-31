package org.example.repository;

import org.example.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    @Query("SELECT q FROM QuestionEntity q WHERE q.puncte >= :minPuncte")
    List<QuestionEntity> gasesteIntrebariCuPunctajMinim(int minPuncte);

    @Query("SELECT q FROM QuestionEntity q ORDER BY q.puncte DESC")
    List<QuestionEntity> toateOrdonateDupaPuncte();
}
