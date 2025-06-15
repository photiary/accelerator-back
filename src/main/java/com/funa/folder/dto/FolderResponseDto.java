package com.funa.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Folder responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponseDto {
    private Long id;
    private String name;
    private String description;
    
    // Simplified representation of parent relationship
    private Long parentId;
    private String parentName;
    
    // Simplified representation of child folders
    private List<FolderSummaryDto> childFolders;
    
    // Simplified representation of features
    private List<FeatureSummaryDto> features;
    
    // Audit fields
    private LocalDateTime createdAt;
    private String createdId;
    private LocalDateTime updatedAt;
    private String updatedId;
    
    /**
     * Summary DTO for child folders.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FolderSummaryDto {
        private Long id;
        private String name;
    }
    
    /**
     * Summary DTO for features.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureSummaryDto {
        private Long id;
        private String name;
    }
}