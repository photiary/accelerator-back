package com.funa.feature.dto;

import com.funa.feature.Feature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Feature entities and DTOs.
 */
@Component
public class FeatureMapper {

    /**
     * Convert a Feature entity to a FeatureResponseDto.
     *
     * @param feature The Feature entity to convert
     * @return The FeatureResponseDto
     */
    public FeatureResponseDto toDto(Feature feature) {
        if (feature == null) {
            return null;
        }

        FeatureResponseDto dto = new FeatureResponseDto();
        dto.setId(feature.getId());
        dto.setName(feature.getName());
        dto.setDescription(feature.getDescription());

        // Set folder information
        if (feature.getFolder() != null) {
            dto.setFolderId(feature.getFolder().getId());
            dto.setFolderName(feature.getFolder().getName());
        }

        // Set template prompt information
        if (feature.getTemplatePrompt() != null) {
            dto.setTemplatePromptId(feature.getTemplatePrompt().getId());
            dto.setTemplatePromptName(feature.getTemplatePrompt().getName());
            dto.setTemplatePromptContent(feature.getTemplatePrompt().getPromptContent());
        }

        // Set sequence diagram information
        if (feature.getSequenceDiagram() != null) {
            dto.setSequenceDiagramId(feature.getSequenceDiagram().getId());
            dto.setSequenceDiagramName(feature.getSequenceDiagram().getName());
            dto.setSequenceDiagramContent(feature.getSequenceDiagram().getSequenceDiagramContent());
        }

        // Set SQL query information
        if (feature.getSqlQuery() != null) {
            dto.setSqlQueryId(feature.getSqlQuery().getId());
            dto.setSqlQueryName(feature.getSqlQuery().getName());
            dto.setSqlQueryContent(feature.getSqlQuery().getQueryContent());
        }

        // Set audit fields
        dto.setCreatedAt(feature.getCreatedAt());
        dto.setCreatedId(feature.getCreatedId());
        dto.setUpdatedAt(feature.getUpdatedAt());
        dto.setUpdatedId(feature.getUpdatedId());

        return dto;
    }

    /**
     * Convert a list of Feature entities to a list of FeatureResponseDtos.
     *
     * @param features The list of Feature entities to convert
     * @return The list of FeatureResponseDtos
     */
    public List<FeatureResponseDto> toDtoList(List<Feature> features) {
        if (features == null) {
            return null;
        }

        return features.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a FeatureRequestDto to a Feature entity.
     * Note: This does not set relationships, only basic properties.
     *
     * @param dto The FeatureRequestDto to convert
     * @return The Feature entity
     */
    public Feature toEntity(FeatureRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Feature feature = new Feature();
        feature.setName(dto.getName());
        feature.setDescription(dto.getDescription());

        return feature;
    }
}
