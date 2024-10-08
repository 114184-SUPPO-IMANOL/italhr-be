package org.apiitalhrbe.repositories.sql;

import org.apiitalhrbe.entities.sql.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long>{

    Optional<DepartmentEntity> findByName(String name);
}
