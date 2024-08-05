package org.apiitalhrbe.repositories.sql;

import org.apiitalhrbe.entities.sql.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);

    Optional<UserEntity> findUserEntityByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE (u.username = :username OR u.email = :email) AND u.password = :password")
    Optional<UserEntity> findUserEntitiesLogin(@Param("username") String username, @Param("email") String email, @Param("password") String password);
}
