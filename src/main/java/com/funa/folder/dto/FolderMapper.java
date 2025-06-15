package com.funa.folder.dto;

import com.funa.folder.Folder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Folder entities and DTOs.
 */
@Component
public class FolderMapper {

    /**
     * Convert a Folder entity to a FolderResponseDto.
     *
     * @param folder The Folder entity to convert
     * @return The FolderResponseDto
     */
    public FolderResponseDto toDto(Folder folder) {
        if (folder == null) {
            return null;
        }

        FolderResponseDto dto = new FolderResponseDto();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setDescription(folder.getDescription());
        
        // Set parent information
        if (folder.getParent() != null) {
            dto.setParentId(folder.getParent().getId());
            dto.setParentName(folder.getParent().getName());
        }
        
        // Set child folders
        if (folder.getFolders() != null && !folder.getFolders().isEmpty()) {
            List<FolderResponseDto.FolderSummaryDto> childFolders = folder.getFolders().stream()
                    .map(childFolder -> {
                        FolderResponseDto.FolderSummaryDto summary = new FolderResponseDto.FolderSummaryDto();
                        summary.setId(childFolder.getId());
                        summary.setName(childFolder.getName());
                        return summary;
                    })
                    .collect(Collectors.toList());
            dto.setChildFolders(childFolders);
        } else {
            dto.setChildFolders(new ArrayList<>());
        }
        
        // Set features
        if (folder.getFeatures() != null && !folder.getFeatures().isEmpty()) {
            List<FolderResponseDto.FeatureSummaryDto> features = folder.getFeatures().stream()
                    .map(feature -> {
                        FolderResponseDto.FeatureSummaryDto summary = new FolderResponseDto.FeatureSummaryDto();
                        summary.setId(feature.getId());
                        summary.setName(feature.getName());
                        return summary;
                    })
                    .collect(Collectors.toList());
            dto.setFeatures(features);
        } else {
            dto.setFeatures(new ArrayList<>());
        }
        
        // Set audit fields
        dto.setCreatedAt(folder.getCreatedAt());
        dto.setCreatedId(folder.getCreatedId());
        dto.setUpdatedAt(folder.getUpdatedAt());
        dto.setUpdatedId(folder.getUpdatedId());
        
        return dto;
    }

    /**
     * Convert a list of Folder entities to a list of FolderResponseDtos.
     *
     * @param folders The list of Folder entities to convert
     * @return The list of FolderResponseDtos
     */
    public List<FolderResponseDto> toDtoList(List<Folder> folders) {
        if (folders == null) {
            return null;
        }
        
        return folders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a FolderRequestDto to a Folder entity.
     * Note: This does not set relationships, only basic properties.
     *
     * @param dto The FolderRequestDto to convert
     * @return The Folder entity
     */
    public Folder toEntity(FolderRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Folder folder = new Folder();
        folder.setName(dto.getName());
        folder.setDescription(dto.getDescription());
        
        return folder;
    }
}