package org.apiitalhrbe.repositories.nosql;

import org.apiitalhrbe.entities.nosql.FunctionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FunctionRepository extends MongoRepository<FunctionEntity, UUID> {
}
