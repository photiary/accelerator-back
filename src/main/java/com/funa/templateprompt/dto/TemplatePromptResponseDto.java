package com.funa.templateprompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for TemplatePrompt responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplatePromptResponseDto {
    private Long id;
    private String name;
    private String promptContent;
    
    // Audit fields
    private LocalDateTime createdAt;
    private String createdId;
    private LocalDateTime updatedAt;
    private String updatedId;
}