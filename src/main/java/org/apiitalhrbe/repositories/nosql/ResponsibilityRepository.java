package org.apiitalhrbe.repositories.nosql;

import org.apiitalhrbe.entities.nosql.ResponsibilityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResponsibilityRepository extends MongoRepository<ResponsibilityEntity, UUID> {
}
