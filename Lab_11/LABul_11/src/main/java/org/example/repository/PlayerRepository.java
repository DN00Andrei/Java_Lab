package org.example.repository;

import org.example.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByNume(String nume);

    List<PlayerEntity> findByeBotTrue();

    @Query("SELECT p FROM PlayerEntity p WHERE p.eBot = false ORDER BY p.nume")
    List<PlayerEntity> gasesteTotiJucatoriiUmani();

    @Modifying
    @Query("UPDATE PlayerEntity p SET p.nume = :numeNou WHERE p.id = :id")
    int redenumestejucator(@Param("id") Long id, @Param("numeNou") String numeNou);
}