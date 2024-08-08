package org.apiitalhrbe.repositories.nosql;

import org.apiitalhrbe.entities.nosql.HardcodedHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HardcodedHistoryRepository extends MongoRepository<HardcodedHistoryEntity, UUID> {
    @Query("{ 'from': ?0, 'to': ?1, 'status': ?2, 'type': ?3 }")
    Optional<HardcodedHistoryEntity> findByFromAndToAndStatusAndType(LocalDate from, LocalDate to, String status, String type);
}
