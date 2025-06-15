package com.funa.sqlquery;

import com.funa.sqlquery.dto.SqlQueryMapper;
import com.funa.sqlquery.dto.SqlQueryRequestDto;
import com.funa.sqlquery.dto.SqlQueryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "SQL Query", description = "SQL Query management API")
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
    @Operation(summary = "Get all SQL queries", description = "Retrieves a list of all SQL queries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved SQL queries",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SqlQueryResponseDto.class)))
    })
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
    @Operation(summary = "Get a SQL query by ID", description = "Retrieves a SQL query by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the SQL query",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SqlQueryResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "SQL query not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SqlQueryResponseDto> getSqlQueryById(
            @Parameter(description = "ID of the SQL query to retrieve") @PathVariable Long id) {
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
    @Operation(summary = "Create a new SQL query", description = "Creates a new SQL query with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "SQL query successfully created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SqlQueryResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<SqlQueryResponseDto> createSqlQuery(
            @Parameter(description = "SQL query data to create", required = true) 
            @RequestBody SqlQueryRequestDto sqlQueryDto) {
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
