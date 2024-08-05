package org.apiitalhrbe.repositories.nosql;

import org.apiitalhrbe.entities.nosql.DepartmentHistoryEntity;
import org.apiitalhrbe.entities.nosql.EmployeeHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentHistoryRepository extends MongoRepository<DepartmentHistoryEntity, UUID> {


    @Query("{ 'from': { $gte: ?0, $lte: ?1 }, 'state': { $eq: ?2 } }")
    Optional<List<DepartmentHistoryEntity>> findByFromBetweenAndState(LocalDate fromDate, LocalDate toDate, String state);
}
