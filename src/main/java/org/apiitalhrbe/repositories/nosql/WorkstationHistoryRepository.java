package org.apiitalhrbe.repositories.nosql;

import org.apiitalhrbe.entities.nosql.EmployeeHistoryEntity;
import org.apiitalhrbe.entities.nosql.WorkstationHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkstationHistoryRepository extends MongoRepository<WorkstationHistoryEntity, UUID> {


    @Query("{ 'from': { $gte: ?0, $lte: ?1 }, 'state': { $eq: ?2 } }")
    Optional<List<WorkstationHistoryEntity>> findByFromBetweenAndState(LocalDate fromDate, LocalDate toDate, String state);
}
