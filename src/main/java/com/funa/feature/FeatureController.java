package com.funa.feature;

import com.funa.feature.dto.FeatureMapper;
import com.funa.feature.dto.FeatureRequestDto;
import com.funa.feature.dto.FeatureResponseDto;
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
    @GetMapping("/{id}")
    public ResponseEntity<FeatureResponseDto> getFeatureById(@PathVariable Long id) {
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
    @GetMapping("/search")
    public ResponseEntity<List<FeatureResponseDto>> findFeaturesByName(@RequestParam String name) {
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
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<FeatureResponseDto>> findFeaturesByFolder(@PathVariable Long folderId) {
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
    @PostMapping
    public ResponseEntity<FeatureResponseDto> createFeature(@RequestBody FeatureRequestDto featureDto) {
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
    @PutMapping("/{id}")
    public ResponseEntity<FeatureResponseDto> updateFeature(
            @PathVariable Long id,
            @RequestBody FeatureRequestDto featureDto) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        try {
            featureService.deleteFeature(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
