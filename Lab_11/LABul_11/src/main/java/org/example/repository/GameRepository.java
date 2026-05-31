package org.example.repository;

import org.example.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Query("SELECT g FROM GameEntity g WHERE g.castigator = :castigator")
    List<GameEntity> gasesteJocuriCastigateDe(String castigator);

    @Query("SELECT g FROM GameEntity g ORDER BY g.inceputLa DESC")
    List<GameEntity> toateOrdonateDupaTimpDesc();
}
