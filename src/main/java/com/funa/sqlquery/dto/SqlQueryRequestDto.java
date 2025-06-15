package com.funa.sqlquery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for SqlQuery creation and update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SqlQueryRequestDto {
    private String name;
    private String queryContent;
}