package com.funa.sequencediagram;

import com.funa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SequenceDiagram entity for managing Mermaid sequence diagram code.
 */
@Entity
@Table(name = "tb_sequence_diagram")
@Getter
@Setter
@NoArgsConstructor
public class SequenceDiagram extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "sequence_diagram_content", columnDefinition = "TEXT", nullable = false)
    private String sequenceDiagramContent;
}