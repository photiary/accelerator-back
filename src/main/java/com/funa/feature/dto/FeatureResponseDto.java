package com.funa.feature.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for Feature responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureResponseDto {
    private Long id;
    private String name;
    private String description;
    
    // Simplified representation of relationships
    private Long folderId;
    private String folderName;
    
    private Long templatePromptId;
    private String templatePromptName;
    
    private Long sequenceDiagramId;
    private String sequenceDiagramName;
    
    private Long sqlQueryId;
    private String sqlQueryName;
    
    // Audit fields
    private LocalDateTime createdAt;
    private String createdId;
    private LocalDateTime updatedAt;
    private String updatedId;
}