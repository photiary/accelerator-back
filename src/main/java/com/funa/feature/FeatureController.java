package com.funa.feature;

import com.funa.feature.dto.FeatureMapper;
import com.funa.feature.dto.FeatureRequestDto;
import com.funa.feature.dto.FeatureResponseDto;
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
 * REST controller for managing features.
 */
@RestController
@RequestMapping("/api/features")
@Tag(name = "Feature", description = "Feature management API")
public class FeatureController {

    private final FeatureService featureService;
    private final FeatureMapper featureMapper;

    @Autowired
    public FeatureController(FeatureService featureService, FeatureMapper featureMapper) {
        this.featureService = featureService;
        this.featureMapper = featureMapper;
    }

    /**
     * Get all features.
     *
     * @return List of all features
     */
    @Operation(summary = "Get all features", description = "Retrieves a list of all features")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved features",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<FeatureResponseDto>> getAllFeatures() {
        List<Feature> features = featureService.getAllFeatures();
        List<FeatureResponseDto> featureDtos = featureMapper.toDtoList(features);
        return ResponseEntity.ok(featureDtos);
    }

    /**
     * Get a feature by ID.
     *
     * @param id The ID of the feature
     * @return The feature
     */
    @Operation(summary = "Get a feature by ID", description = "Retrieves a feature by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the feature",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Feature not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeatureResponseDto> getFeatureById(
            @Parameter(description = "ID of the feature to retrieve") @PathVariable Long id) {
        try {
            Feature feature = featureService.getFeatureById(id);
            FeatureResponseDto featureDto = featureMapper.toDto(feature);
            return ResponseEntity.ok(featureDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find features by name.
     *
     * @param name The name to search for
     * @return List of matching features
     */
    @Operation(summary = "Find features by name", description = "Retrieves a list of features matching the provided name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved features",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<FeatureResponseDto>> findFeaturesByName(@Parameter(description = "Name to search for") @RequestParam String name) {
        List<Feature> features = featureService.findFeaturesByName(name);
        List<FeatureResponseDto> featureDtos = featureMapper.toDtoList(features);
        return ResponseEntity.ok(featureDtos);
    }

    /**
     * Find features by folder.
     *
     * @param folderId The folder ID to search in
     * @return List of features in the folder
     */
    @Operation(summary = "Find features by folder", description = "Retrieves a list of features in the specified folder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved features",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Folder not found",
                content = @Content)
    })
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<FeatureResponseDto>> findFeaturesByFolder(@Parameter(description = "ID of the folder to search in") @PathVariable Long folderId) {
        try {
            List<Feature> features = featureService.findFeaturesByFolder(folderId);
            List<FeatureResponseDto> featureDtos = featureMapper.toDtoList(features);
            return ResponseEntity.ok(featureDtos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new feature.
     *
     * @param featureDto The feature data to create
     * @return The created feature
     */
    @Operation(summary = "Create a new feature", description = "Creates a new feature with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feature successfully created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<FeatureResponseDto> createFeature(
            @Parameter(description = "Feature data to create", required = true) 
            @RequestBody FeatureRequestDto featureDto) {
        try {
            // Convert DTO to entity
            Feature feature = featureMapper.toEntity(featureDto);

            // Create feature using service
            Feature createdFeature = featureService.createFeature(
                    feature, 
                    featureDto.getFolderId(),
                    featureDto.getTemplatePromptId(),
                    featureDto.getSequenceDiagramId(),
                    featureDto.getSqlQueryId());

            // Convert entity back to DTO
            FeatureResponseDto responseDto = featureMapper.toDto(createdFeature);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing feature.
     *
     * @param id The ID of the feature to update
     * @param featureDto The updated feature data
     * @return The updated feature
     */
    @Operation(summary = "Update a feature", description = "Updates an existing feature with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature successfully updated",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Feature not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<FeatureResponseDto> updateFeature(
            @Parameter(description = "ID of the feature to update") @PathVariable Long id,
            @Parameter(description = "Updated feature data", required = true) @RequestBody FeatureRequestDto featureDto) {
        try {
            // Convert DTO to entity
            Feature feature = featureMapper.toEntity(featureDto);

            // Update feature using service
            Feature updatedFeature = featureService.updateFeature(
                    id, 
                    feature, 
                    featureDto.getFolderId(),
                    featureDto.getTemplatePromptId(),
                    featureDto.getSequenceDiagramId(),
                    featureDto.getSqlQueryId());

            // Convert entity back to DTO
            FeatureResponseDto responseDto = featureMapper.toDto(updatedFeature);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a feature by ID.
     *
     * @param id The ID of the feature to delete
     * @return No content if successful, not found if the feature doesn't exist
     */
    @Operation(summary = "Delete a feature", description = "Deletes a feature by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feature successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Feature not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@Parameter(description = "ID of the feature to delete") @PathVariable Long id) {
        try {
            featureService.deleteFeature(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
