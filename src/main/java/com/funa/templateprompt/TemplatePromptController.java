package com.funa.templateprompt;

import com.funa.templateprompt.dto.TemplatePromptMapper;
import com.funa.templateprompt.dto.TemplatePromptRequestDto;
import com.funa.templateprompt.dto.TemplatePromptResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing template prompts.
 */
@RestController
@RequestMapping("/api/template-prompts")
@Tag(name = "Template Prompt", description = "Template Prompt management API")
public class TemplatePromptController {

    private final TemplatePromptService templatePromptService;
    private final TemplatePromptMapper templatePromptMapper;

    @Autowired
    public TemplatePromptController(TemplatePromptService templatePromptService, TemplatePromptMapper templatePromptMapper) {
        this.templatePromptService = templatePromptService;
        this.templatePromptMapper = templatePromptMapper;
    }

    /**
     * Get all template prompts.
     *
     * @return List of all template prompts
     */
    @Operation(summary = "Get all template prompts", description = "Retrieves a list of all template prompts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved template prompts",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TemplatePromptResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<TemplatePromptResponseDto>> getAllTemplatePrompts() {
        List<TemplatePrompt> templatePrompts = templatePromptService.getAllTemplatePrompts();
        List<TemplatePromptResponseDto> templatePromptDtos = templatePromptMapper.toDtoList(templatePrompts);
        return ResponseEntity.ok(templatePromptDtos);
    }

    /**
     * Get a template prompt by ID.
     *
     * @param id The ID of the template prompt
     * @return The template prompt
     */
    @Operation(summary = "Get a template prompt by ID", description = "Retrieves a template prompt by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the template prompt",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TemplatePromptResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Template prompt not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TemplatePromptResponseDto> getTemplatePromptById(
            @Parameter(description = "ID of the template prompt to retrieve") @PathVariable Long id) {
        try {
            TemplatePrompt templatePrompt = templatePromptService.getTemplatePromptById(id);
            TemplatePromptResponseDto templatePromptDto = templatePromptMapper.toDto(templatePrompt);
            return ResponseEntity.ok(templatePromptDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find template prompts by name.
     *
     * @param name The name to search for
     * @return List of matching template prompts
     */
    @GetMapping("/search")
    public ResponseEntity<List<TemplatePromptResponseDto>> findTemplatePromptsByName(@RequestParam String name) {
        List<TemplatePrompt> templatePrompts = templatePromptService.findTemplatePromptsByName(name);
        List<TemplatePromptResponseDto> templatePromptDtos = templatePromptMapper.toDtoList(templatePrompts);
        return ResponseEntity.ok(templatePromptDtos);
    }

    /**
     * Create a new template prompt.
     *
     * @param templatePromptDto The template prompt data to create
     * @return The created template prompt
     */
    @Operation(summary = "Create a new template prompt", description = "Creates a new template prompt with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Template prompt successfully created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TemplatePromptResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<TemplatePromptResponseDto> createTemplatePrompt(
            @Parameter(description = "Template prompt data to create", required = true) 
            @RequestBody TemplatePromptRequestDto templatePromptDto) {
        // Convert DTO to entity
        TemplatePrompt templatePrompt = templatePromptMapper.toEntity(templatePromptDto);

        // Create template prompt using service
        TemplatePrompt createdTemplatePrompt = templatePromptService.createTemplatePrompt(templatePrompt);

        // Convert entity back to DTO
        TemplatePromptResponseDto responseDto = templatePromptMapper.toDto(createdTemplatePrompt);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Update an existing template prompt.
     *
     * @param id The ID of the template prompt to update
     * @param templatePromptDto The updated template prompt data
     * @return The updated template prompt
     */
    @PutMapping("/{id}")
    public ResponseEntity<TemplatePromptResponseDto> updateTemplatePrompt(
            @PathVariable Long id,
            @RequestBody TemplatePromptRequestDto templatePromptDto) {
        try {
            // Convert DTO to entity
            TemplatePrompt templatePrompt = templatePromptMapper.toEntity(templatePromptDto);

            // Update template prompt using service
            TemplatePrompt updatedTemplatePrompt = templatePromptService.updateTemplatePrompt(id, templatePrompt);

            // Convert entity back to DTO
            TemplatePromptResponseDto responseDto = templatePromptMapper.toDto(updatedTemplatePrompt);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a template prompt by ID.
     *
     * @param id The ID of the template prompt to delete
     * @return No content if successful, not found if the template prompt doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplatePrompt(@PathVariable Long id) {
        try {
            templatePromptService.deleteTemplatePrompt(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
