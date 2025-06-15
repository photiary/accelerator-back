package com.funa.sqlquery;

import com.funa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SqlQuery entity for managing SQL queries.
 */
@Entity
@Table(name = "tb_sql_query")
@Getter
@Setter
@NoArgsConstructor
public class SqlQuery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "query_content", columnDefinition = "TEXT", nullable = false)
    private String queryContent;
}