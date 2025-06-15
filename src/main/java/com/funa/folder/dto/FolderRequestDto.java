package com.funa.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Folder creation and update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderRequestDto {
    private String name;
    private String description;
    
    // We use ID instead of entity reference for parent relationship
    private Long parentId;
}