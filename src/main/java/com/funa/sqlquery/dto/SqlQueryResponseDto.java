package com.funa.sqlquery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for SqlQuery responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SqlQueryResponseDto {
    private Long id;
    private String name;
    private String queryContent;
    
    // Audit fields
    private LocalDateTime createdAt;
    private String createdId;
    private LocalDateTime updatedAt;
    private String updatedId;
}