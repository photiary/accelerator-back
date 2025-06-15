package com.funa.sqlquery;

import com.funa.sqlquery.dto.SqlQueryMapper;
import com.funa.sqlquery.dto.SqlQueryRequestDto;
import com.funa.sqlquery.dto.SqlQueryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing SQL queries.
 */
@RestController
@RequestMapping("/api/sql-queries")
public class SqlQueryController {

    private final SqlQueryService sqlQueryService;
    private final SqlQueryMapper sqlQueryMapper;

    @Autowired
    public SqlQueryController(SqlQueryService sqlQueryService, SqlQueryMapper sqlQueryMapper) {
        this.sqlQueryService = sqlQueryService;
        this.sqlQueryMapper = sqlQueryMapper;
    }

    /**
     * Get all SQL queries.
     *
     * @return List of all SQL queries
     */
    @GetMapping
    public ResponseEntity<List<SqlQueryResponseDto>> getAllSqlQueries() {
        List<SqlQuery> sqlQueries = sqlQueryService.getAllSqlQueries();
        List<SqlQueryResponseDto> sqlQueryDtos = sqlQueryMapper.toDtoList(sqlQueries);
        return ResponseEntity.ok(sqlQueryDtos);
    }

    /**
     * Get a SQL query by ID.
     *
     * @param id The ID of the SQL query
     * @return The SQL query
     */
    @GetMapping("/{id}")
    public ResponseEntity<SqlQueryResponseDto> getSqlQueryById(@PathVariable Long id) {
        try {
            SqlQuery sqlQuery = sqlQueryService.getSqlQueryById(id);
            SqlQueryResponseDto sqlQueryDto = sqlQueryMapper.toDto(sqlQuery);
            return ResponseEntity.ok(sqlQueryDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find SQL queries by name.
     *
     * @param name The name to search for
     * @return List of matching SQL queries
     */
    @GetMapping("/search")
    public ResponseEntity<List<SqlQueryResponseDto>> findSqlQueriesByName(@RequestParam String name) {
        List<SqlQuery> sqlQueries = sqlQueryService.findSqlQueriesByName(name);
        List<SqlQueryResponseDto> sqlQueryDtos = sqlQueryMapper.toDtoList(sqlQueries);
        return ResponseEntity.ok(sqlQueryDtos);
    }

    /**
     * Create a new SQL query.
     *
     * @param sqlQueryDto The SQL query data to create
     * @return The created SQL query
     */
    @PostMapping
    public ResponseEntity<SqlQueryResponseDto> createSqlQuery(@RequestBody SqlQueryRequestDto sqlQueryDto) {
        // Convert DTO to entity
        SqlQuery sqlQuery = sqlQueryMapper.toEntity(sqlQueryDto);

        // Create SQL query using service
        SqlQuery createdSqlQuery = sqlQueryService.createSqlQuery(sqlQuery);

        // Convert entity back to DTO
        SqlQueryResponseDto responseDto = sqlQueryMapper.toDto(createdSqlQuery);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Update an existing SQL query.
     *
     * @param id The ID of the SQL query to update
     * @param sqlQueryDto The updated SQL query data
     * @return The updated SQL query
     */
    @PutMapping("/{id}")
    public ResponseEntity<SqlQueryResponseDto> updateSqlQuery(
            @PathVariable Long id,
            @RequestBody SqlQueryRequestDto sqlQueryDto) {
        try {
            // Convert DTO to entity
            SqlQuery sqlQuery = sqlQueryMapper.toEntity(sqlQueryDto);

            // Update SQL query using service
            SqlQuery updatedSqlQuery = sqlQueryService.updateSqlQuery(id, sqlQuery);

            // Convert entity back to DTO
            SqlQueryResponseDto responseDto = sqlQueryMapper.toDto(updatedSqlQuery);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a SQL query by ID.
     *
     * @param id The ID of the SQL query to delete
     * @return No content if successful, not found if the SQL query doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSqlQuery(@PathVariable Long id) {
        try {
            sqlQueryService.deleteSqlQuery(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
