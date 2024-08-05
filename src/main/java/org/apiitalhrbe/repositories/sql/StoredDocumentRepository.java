package org.apiitalhrbe.repositories.sql;

import org.apiitalhrbe.entities.sql.StoredDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredDocumentRepository extends JpaRepository<StoredDocumentEntity, Long> {
}
