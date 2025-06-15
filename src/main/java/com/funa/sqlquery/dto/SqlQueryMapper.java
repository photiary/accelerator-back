package com.funa.sqlquery.dto;

import com.funa.sqlquery.SqlQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between SqlQuery entities and DTOs.
 */
@Component
public class SqlQueryMapper {

    /**
     * Convert a SqlQuery entity to a SqlQueryResponseDto.
     *
     * @param sqlQuery The SqlQuery entity to convert
     * @return The SqlQueryResponseDto
     */
    public SqlQueryResponseDto toDto(SqlQuery sqlQuery) {
        if (sqlQuery == null) {
            return null;
        }

        SqlQueryResponseDto dto = new SqlQueryResponseDto();
        dto.setId(sqlQuery.getId());
        dto.setName(sqlQuery.getName());
        dto.setQueryContent(sqlQuery.getQueryContent());
        
        // Set audit fields
        dto.setCreatedAt(sqlQuery.getCreatedAt());
        dto.setCreatedId(sqlQuery.getCreatedId());
        dto.setUpdatedAt(sqlQuery.getUpdatedAt());
        dto.setUpdatedId(sqlQuery.getUpdatedId());
        
        return dto;
    }

    /**
     * Convert a list of SqlQuery entities to a list of SqlQueryResponseDtos.
     *
     * @param sqlQueries The list of SqlQuery entities to convert
     * @return The list of SqlQueryResponseDtos
     */
    public List<SqlQueryResponseDto> toDtoList(List<SqlQuery> sqlQueries) {
        if (sqlQueries == null) {
            return null;
        }
        
        return sqlQueries.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a SqlQueryRequestDto to a SqlQuery entity.
     *
     * @param dto The SqlQueryRequestDto to convert
     * @return The SqlQuery entity
     */
    public SqlQuery toEntity(SqlQueryRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        SqlQuery sqlQuery = new SqlQuery();
        sqlQuery.setName(dto.getName());
        sqlQuery.setQueryContent(dto.getQueryContent());
        
        return sqlQuery;
    }
}