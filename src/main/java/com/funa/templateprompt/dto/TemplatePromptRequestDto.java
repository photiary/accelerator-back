package com.funa.templateprompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for TemplatePrompt creation and update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplatePromptRequestDto {
    private String name;
    private String promptContent;
}