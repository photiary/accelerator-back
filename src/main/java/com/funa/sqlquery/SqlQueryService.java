package com.funa.sqlquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for managing SQL queries.
 */
@Service
@Transactional
public class SqlQueryService {

    private final SqlQueryRepository sqlQueryRepository;

    @Autowired
    public SqlQueryService(SqlQueryRepository sqlQueryRepository) {
        this.sqlQueryRepository = sqlQueryRepository;
    }

    /**
     * Get all SQL queries.
     *
     * @return List of all SQL queries
     */
    @Transactional(readOnly = true)
    public List<SqlQuery> getAllSqlQueries() {
        return sqlQueryRepository.findAll();
    }

    /**
     * Get a SQL query by ID.
     *
     * @param id The ID of the SQL query
     * @return The SQL query
     * @throws NoSuchElementException if the SQL query is not found
     */
    @Transactional(readOnly = true)
    public SqlQuery getSqlQueryById(Long id) {
        return sqlQueryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SQL query not found with ID: " + id));
    }

    /**
     * Find SQL queries by name containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching SQL queries
     */
    @Transactional(readOnly = true)
    public List<SqlQuery> findSqlQueriesByName(String name) {
        return sqlQueryRepository.findByNameContaining(name);
    }

    /**
     * Create a new SQL query.
     *
     * @param sqlQuery The SQL query to create
     * @return The created SQL query
     */
    public SqlQuery createSqlQuery(SqlQuery sqlQuery) {
        return sqlQueryRepository.save(sqlQuery);
    }

    /**
     * Update an existing SQL query.
     *
     * @param id The ID of the SQL query to update
     * @param sqlQuery The updated SQL query data
     * @return The updated SQL query
     * @throws NoSuchElementException if the SQL query is not found
     */
    public SqlQuery updateSqlQuery(Long id, SqlQuery sqlQuery) {
        SqlQuery existingSqlQuery = getSqlQueryById(id);
        existingSqlQuery.setName(sqlQuery.getName());
        existingSqlQuery.setQueryContent(sqlQuery.getQueryContent());
        return sqlQueryRepository.save(existingSqlQuery);
    }

    /**
     * Delete a SQL query by ID.
     *
     * @param id The ID of the SQL query to delete
     * @throws NoSuchElementException if the SQL query is not found
     */
    public void deleteSqlQuery(Long id) {
        if (!sqlQueryRepository.existsById(id)) {
            throw new NoSuchElementException("SQL query not found with ID: " + id);
        }
        sqlQueryRepository.deleteById(id);
    }
}