package com.funa.feature.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Feature creation and update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureRequestDto {
    private String name;
    private String description;
    
    // We use IDs instead of entity references for relationships
    private Long folderId;
    private Long templatePromptId;
    private Long sequenceDiagramId;
    private Long sqlQueryId;
}