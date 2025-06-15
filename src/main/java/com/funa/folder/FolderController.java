package com.funa.folder;

import com.funa.feature.Feature;
import com.funa.feature.dto.FeatureMapper;
import com.funa.feature.dto.FeatureRequestDto;
import com.funa.feature.dto.FeatureResponseDto;
import com.funa.folder.dto.FolderMapper;
import com.funa.folder.dto.FolderRequestDto;
import com.funa.folder.dto.FolderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing folders.
 */
@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;
    private final FolderMapper folderMapper;
    private final FeatureMapper featureMapper;

    @Autowired
    public FolderController(FolderService folderService, FolderMapper folderMapper, FeatureMapper featureMapper) {
        this.folderService = folderService;
        this.folderMapper = folderMapper;
        this.featureMapper = featureMapper;
    }

    /**
     * Get all folders.
     *
     * @return List of all folders
     */
    @GetMapping
    public ResponseEntity<List<FolderResponseDto>> getAllFolders() {
        List<Folder> folders = folderService.getAllFolders();
        List<FolderResponseDto> folderDtos = folderMapper.toDtoList(folders);
        return ResponseEntity.ok(folderDtos);
    }

    /**
     * Get all root folders (folders with no parent).
     *
     * @return List of root folders
     */
    @GetMapping("/root")
    public ResponseEntity<List<FolderResponseDto>> getRootFolders() {
        List<Folder> rootFolders = folderService.getRootFolders();
        List<FolderResponseDto> folderDtos = folderMapper.toDtoList(rootFolders);
        return ResponseEntity.ok(folderDtos);
    }

    /**
     * Get a folder by ID.
     *
     * @param id The ID of the folder
     * @return The folder
     */
    @GetMapping("/{id}")
    public ResponseEntity<FolderResponseDto> getFolderById(@PathVariable Long id) {
        try {
            Folder folder = folderService.getFolderById(id);
            FolderResponseDto folderDto = folderMapper.toDto(folder);
            return ResponseEntity.ok(folderDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find folders by name.
     *
     * @param name The name to search for
     * @return List of matching folders
     */
    @GetMapping("/search")
    public ResponseEntity<List<FolderResponseDto>> findFoldersByName(@RequestParam String name) {
        List<Folder> folders = folderService.findFoldersByName(name);
        List<FolderResponseDto> folderDtos = folderMapper.toDtoList(folders);
        return ResponseEntity.ok(folderDtos);
    }

    /**
     * Find child folders of a parent folder.
     *
     * @param parentId The ID of the parent folder
     * @return List of child folders
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<FolderResponseDto>> findChildFolders(@PathVariable Long parentId) {
        try {
            List<Folder> childFolders = folderService.findChildFolders(parentId);
            List<FolderResponseDto> folderDtos = folderMapper.toDtoList(childFolders);
            return ResponseEntity.ok(folderDtos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new folder.
     *
     * @param folderDto The folder data to create
     * @return The created folder
     */
    @PostMapping
    public ResponseEntity<FolderResponseDto> createFolder(@RequestBody FolderRequestDto folderDto) {
        try {
            // Convert DTO to entity
            Folder folder = folderMapper.toEntity(folderDto);

            // Create folder using service
            Folder createdFolder = folderService.createFolder(folder, folderDto.getParentId());

            // Convert entity back to DTO
            FolderResponseDto responseDto = folderMapper.toDto(createdFolder);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing folder.
     *
     * @param id The ID of the folder to update
     * @param folderDto The updated folder data
     * @return The updated folder
     */
    @PutMapping("/{id}")
    public ResponseEntity<FolderResponseDto> updateFolder(
            @PathVariable Long id,
            @RequestBody FolderRequestDto folderDto) {
        try {
            // Convert DTO to entity
            Folder folder = folderMapper.toEntity(folderDto);

            // Update folder using service
            Folder updatedFolder = folderService.updateFolder(id, folder, folderDto.getParentId());

            // Convert entity back to DTO
            FolderResponseDto responseDto = folderMapper.toDto(updatedFolder);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a folder by ID.
     *
     * @param id The ID of the folder to delete
     * @return No content if successful, not found if the folder doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        try {
            folderService.deleteFolder(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add a feature to a folder.
     *
     * @param folderId The ID of the folder
     * @param featureDto The feature data to add
     * @return The added feature
     */
    @PostMapping("/{folderId}/features")
    public ResponseEntity<FeatureResponseDto> addFeatureToFolder(
            @PathVariable Long folderId,
            @RequestBody FeatureRequestDto featureDto) {
        try {
            // Convert DTO to entity
            Feature feature = featureMapper.toEntity(featureDto);

            // Add feature to folder using service
            Feature addedFeature = folderService.addFeatureToFolder(folderId, feature);

            // Convert entity back to DTO
            FeatureResponseDto responseDto = featureMapper.toDto(addedFeature);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Move a feature to a different folder.
     *
     * @param folderId The ID of the new folder
     * @param featureId The ID of the feature to move
     * @return The moved feature
     */
    @PutMapping("/{folderId}/features/{featureId}")
    public ResponseEntity<FeatureResponseDto> moveFeatureToFolder(
            @PathVariable Long folderId,
            @PathVariable Long featureId) {
        try {
            // Move feature to folder using service
            Feature movedFeature = folderService.moveFeatureToFolder(featureId, folderId);

            // Convert entity to DTO
            FeatureResponseDto responseDto = featureMapper.toDto(movedFeature);

            return ResponseEntity.ok(responseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
