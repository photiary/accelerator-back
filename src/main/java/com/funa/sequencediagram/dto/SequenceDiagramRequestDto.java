package com.funa.sequencediagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for SequenceDiagram creation and update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequenceDiagramRequestDto {
    private String name;
    private String sequenceDiagramContent;
}