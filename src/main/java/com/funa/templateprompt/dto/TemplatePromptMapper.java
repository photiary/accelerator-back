package com.funa.templateprompt.dto;

import com.funa.templateprompt.TemplatePrompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between TemplatePrompt entities and DTOs.
 */
@Component
public class TemplatePromptMapper {

    /**
     * Convert a TemplatePrompt entity to a TemplatePromptResponseDto.
     *
     * @param templatePrompt The TemplatePrompt entity to convert
     * @return The TemplatePromptResponseDto
     */
    public TemplatePromptResponseDto toDto(TemplatePrompt templatePrompt) {
        if (templatePrompt == null) {
            return null;
        }

        TemplatePromptResponseDto dto = new TemplatePromptResponseDto();
        dto.setId(templatePrompt.getId());
        dto.setName(templatePrompt.getName());
        dto.setPromptContent(templatePrompt.getPromptContent());
        
        // Set audit fields
        dto.setCreatedAt(templatePrompt.getCreatedAt());
        dto.setCreatedId(templatePrompt.getCreatedId());
        dto.setUpdatedAt(templatePrompt.getUpdatedAt());
        dto.setUpdatedId(templatePrompt.getUpdatedId());
        
        return dto;
    }

    /**
     * Convert a list of TemplatePrompt entities to a list of TemplatePromptResponseDtos.
     *
     * @param templatePrompts The list of TemplatePrompt entities to convert
     * @return The list of TemplatePromptResponseDtos
     */
    public List<TemplatePromptResponseDto> toDtoList(List<TemplatePrompt> templatePrompts) {
        if (templatePrompts == null) {
            return null;
        }
        
        return templatePrompts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a TemplatePromptRequestDto to a TemplatePrompt entity.
     *
     * @param dto The TemplatePromptRequestDto to convert
     * @return The TemplatePrompt entity
     */
    public TemplatePrompt toEntity(TemplatePromptRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        TemplatePrompt templatePrompt = new TemplatePrompt();
        templatePrompt.setName(dto.getName());
        templatePrompt.setPromptContent(dto.getPromptContent());
        
        return templatePrompt;
    }
}