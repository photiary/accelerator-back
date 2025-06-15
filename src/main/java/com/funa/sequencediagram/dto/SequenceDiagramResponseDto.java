package com.funa.sequencediagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for SequenceDiagram responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequenceDiagramResponseDto {
    private Long id;
    private String name;
    private String sequenceDiagramContent;
    
    // Audit fields
    private LocalDateTime createdAt;
    private String createdId;
    private LocalDateTime updatedAt;
    private String updatedId;
}