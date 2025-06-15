package com.funa.sqlquery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SqlQuery entity.
 */
@Repository
public interface SqlQueryRepository extends JpaRepository<SqlQuery, Long> {

    /**
     * Find a SQL query by name.
     *
     * @param name The name to search for
     * @return The SQL query if found
     */
    Optional<SqlQuery> findByName(String name);

    /**
     * Find SQL queries with names containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching SQL queries
     */
    List<SqlQuery> findByNameContaining(String name);
}