package com.funa.sequencediagram;

import com.funa.sequencediagram.dto.SequenceDiagramMapper;
import com.funa.sequencediagram.dto.SequenceDiagramRequestDto;
import com.funa.sequencediagram.dto.SequenceDiagramResponseDto;
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
 * REST controller for managing sequence diagrams.
 */
@RestController
@RequestMapping("/api/sequence-diagrams")
@Tag(name = "Sequence Diagram", description = "Sequence Diagram management API")
public class SequenceDiagramController {

    private final SequenceDiagramService sequenceDiagramService;
    private final SequenceDiagramMapper sequenceDiagramMapper;

    @Autowired
    public SequenceDiagramController(SequenceDiagramService sequenceDiagramService, SequenceDiagramMapper sequenceDiagramMapper) {
        this.sequenceDiagramService = sequenceDiagramService;
        this.sequenceDiagramMapper = sequenceDiagramMapper;
    }

    /**
     * Get all sequence diagrams.
     *
     * @return List of all sequence diagrams
     */
    @Operation(summary = "Get all sequence diagrams", description = "Retrieves a list of all sequence diagrams")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sequence diagrams",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SequenceDiagramResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<SequenceDiagramResponseDto>> getAllSequenceDiagrams() {
        List<SequenceDiagram> sequenceDiagrams = sequenceDiagramService.getAllSequenceDiagrams();
        List<SequenceDiagramResponseDto> sequenceDiagramDtos = sequenceDiagramMapper.toDtoList(sequenceDiagrams);
        return ResponseEntity.ok(sequenceDiagramDtos);
    }

    /**
     * Get a sequence diagram by ID.
     *
     * @param id The ID of the sequence diagram
     * @return The sequence diagram
     */
    @Operation(summary = "Get a sequence diagram by ID", description = "Retrieves a sequence diagram by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the sequence diagram",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SequenceDiagramResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Sequence diagram not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SequenceDiagramResponseDto> getSequenceDiagramById(
            @Parameter(description = "ID of the sequence diagram to retrieve") @PathVariable Long id) {
        try {
            SequenceDiagram sequenceDiagram = sequenceDiagramService.getSequenceDiagramById(id);
            SequenceDiagramResponseDto sequenceDiagramDto = sequenceDiagramMapper.toDto(sequenceDiagram);
            return ResponseEntity.ok(sequenceDiagramDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find sequence diagrams by name.
     *
     * @param name The name to search for
     * @return List of matching sequence diagrams
     */
    @GetMapping("/search")
    public ResponseEntity<List<SequenceDiagramResponseDto>> findSequenceDiagramsByName(@RequestParam String name) {
        List<SequenceDiagram> sequenceDiagrams = sequenceDiagramService.findSequenceDiagramsByName(name);
        List<SequenceDiagramResponseDto> sequenceDiagramDtos = sequenceDiagramMapper.toDtoList(sequenceDiagrams);
        return ResponseEntity.ok(sequenceDiagramDtos);
    }

    /**
     * Create a new sequence diagram.
     *
     * @param sequenceDiagramDto The sequence diagram data to create
     * @return The created sequence diagram
     */
    @Operation(summary = "Create a new sequence diagram", description = "Creates a new sequence diagram with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sequence diagram successfully created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SequenceDiagramResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<SequenceDiagramResponseDto> createSequenceDiagram(
            @Parameter(description = "Sequence diagram data to create", required = true) 
            @RequestBody SequenceDiagramRequestDto sequenceDiagramDto) {
        // Convert DTO to entity
        SequenceDiagram sequenceDiagram = sequenceDiagramMapper.toEntity(sequenceDiagramDto);

        // Create sequence diagram using service
        SequenceDiagram createdSequenceDiagram = sequenceDiagramService.createSequenceDiagram(sequenceDiagram);

        // Convert entity back to DTO
        SequenceDiagramResponseDto responseDto = sequenceDiagramMapper.toDto(createdSequenceDiagram);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Update an existing sequence diagram.
     *
     * @param id The ID of the sequence diagram to update
     * @param sequenceDiagramDto The updated sequence diagram data
     * @return The updated sequence diagram
     */
    @PutMapping("/{id}")
    public ResponseEntity<SequenceDiagramResponseDto> updateSequenceDiagram(
            @PathVariable Long id,
            @RequestBody SequenceDiagramRequestDto sequenceDiagramDto) {
        try {
            // Convert DTO to entity
            SequenceDiagram sequenceDiagram = sequenceDiagramMapper.toEntity(sequenceDiagramDto);

            // Update sequence diagram using service
            SequenceDiagram updatedSequenceDiagram = sequenceDiagramService.updateSequenceDiagram(id, sequenceDiagram);

            // Convert entity back to DTO
            SequenceDiagramResponseDto responseDto = sequenceDiagramMapper.toDto(updatedSequenceDiagram);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a sequence diagram by ID.
     *
     * @param id The ID of the sequence diagram to delete
     * @return No content if successful, not found if the sequence diagram doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSequenceDiagram(@PathVariable Long id) {
        try {
            sequenceDiagramService.deleteSequenceDiagram(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
