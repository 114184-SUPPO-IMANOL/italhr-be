package org.apiitalhrbe.repositories.sql;

import org.apiitalhrbe.entities.sql.WorkstationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkstationRepository extends JpaRepository<WorkstationEntity, Long> {

    Optional<WorkstationEntity> findWorkstationEntityByName(String name);
}
