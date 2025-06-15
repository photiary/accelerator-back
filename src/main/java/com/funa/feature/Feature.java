package com.funa.feature;

import com.funa.common.entity.BaseEntity;
import com.funa.folder.Folder;
import com.funa.sequencediagram.SequenceDiagram;
import com.funa.sqlquery.SqlQuery;
import com.funa.templateprompt.TemplatePrompt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Feature entity for managing features that combine TemplatePrompt, SequenceDiagram, and SqlQuery.
 */
@Entity
@Table(name = "tb_feature")
@Getter
@Setter
@NoArgsConstructor
public class Feature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_prompt_id")
    private TemplatePrompt templatePrompt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sequence_diagram_id")
    private SequenceDiagram sequenceDiagram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sql_query_id")
    private SqlQuery sqlQuery;
}