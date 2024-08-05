package org.apiitalhrbe.repositories.sql;

import org.apiitalhrbe.entities.sql.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByDocumentNumber(String documentNumber);
}
