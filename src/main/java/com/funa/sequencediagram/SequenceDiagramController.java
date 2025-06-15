package com.funa.sequencediagram;

import com.funa.sequencediagram.dto.SequenceDiagramMapper;
import com.funa.sequencediagram.dto.SequenceDiagramRequestDto;
import com.funa.sequencediagram.dto.SequenceDiagramResponseDto;
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
    @GetMapping("/{id}")
    public ResponseEntity<SequenceDiagramResponseDto> getSequenceDiagramById(@PathVariable Long id) {
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
    @PostMapping
    public ResponseEntity<SequenceDiagramResponseDto> createSequenceDiagram(@RequestBody SequenceDiagramRequestDto sequenceDiagramDto) {
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
