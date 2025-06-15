package com.funa.sequencediagram.dto;

import com.funa.sequencediagram.SequenceDiagram;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between SequenceDiagram entities and DTOs.
 */
@Component
public class SequenceDiagramMapper {

    /**
     * Convert a SequenceDiagram entity to a SequenceDiagramResponseDto.
     *
     * @param sequenceDiagram The SequenceDiagram entity to convert
     * @return The SequenceDiagramResponseDto
     */
    public SequenceDiagramResponseDto toDto(SequenceDiagram sequenceDiagram) {
        if (sequenceDiagram == null) {
            return null;
        }

        SequenceDiagramResponseDto dto = new SequenceDiagramResponseDto();
        dto.setId(sequenceDiagram.getId());
        dto.setName(sequenceDiagram.getName());
        dto.setSequenceDiagramContent(sequenceDiagram.getSequenceDiagramContent());
        
        // Set audit fields
        dto.setCreatedAt(sequenceDiagram.getCreatedAt());
        dto.setCreatedId(sequenceDiagram.getCreatedId());
        dto.setUpdatedAt(sequenceDiagram.getUpdatedAt());
        dto.setUpdatedId(sequenceDiagram.getUpdatedId());
        
        return dto;
    }

    /**
     * Convert a list of SequenceDiagram entities to a list of SequenceDiagramResponseDtos.
     *
     * @param sequenceDiagrams The list of SequenceDiagram entities to convert
     * @return The list of SequenceDiagramResponseDtos
     */
    public List<SequenceDiagramResponseDto> toDtoList(List<SequenceDiagram> sequenceDiagrams) {
        if (sequenceDiagrams == null) {
            return null;
        }
        
        return sequenceDiagrams.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a SequenceDiagramRequestDto to a SequenceDiagram entity.
     *
     * @param dto The SequenceDiagramRequestDto to convert
     * @return The SequenceDiagram entity
     */
    public SequenceDiagram toEntity(SequenceDiagramRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        SequenceDiagram sequenceDiagram = new SequenceDiagram();
        sequenceDiagram.setName(dto.getName());
        sequenceDiagram.setSequenceDiagramContent(dto.getSequenceDiagramContent());
        
        return sequenceDiagram;
    }
}