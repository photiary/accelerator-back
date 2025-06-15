package com.funa.folder;

import com.funa.feature.Feature;
import com.funa.feature.dto.FeatureMapper;
import com.funa.feature.dto.FeatureRequestDto;
import com.funa.feature.dto.FeatureResponseDto;
import com.funa.folder.dto.FolderMapper;
import com.funa.folder.dto.FolderRequestDto;
import com.funa.folder.dto.FolderResponseDto;
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
 * REST controller for managing folders.
 */
@RestController
@RequestMapping("/api/folders")
@Tag(name = "Folder", description = "Folder management API")
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
    @Operation(summary = "Get all folders", description = "Retrieves a list of all folders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class)))
    })
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
    @Operation(summary = "Get all root folders", description = "Retrieves a list of all root folders (folders with no parent)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved root folders",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class)))
    })
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
    @Operation(summary = "Get a folder by ID", description = "Retrieves a folder by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the folder",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Folder not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FolderResponseDto> getFolderById(
            @Parameter(description = "ID of the folder to retrieve") @PathVariable Long id) {
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
    @Operation(summary = "Find folders by name", description = "Retrieves a list of folders matching the provided name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<FolderResponseDto>> findFoldersByName(@Parameter(description = "Name to search for") @RequestParam String name) {
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
    @Operation(summary = "Find child folders", description = "Retrieves a list of child folders for the specified parent folder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved child folders",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Parent folder not found",
                content = @Content)
    })
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<FolderResponseDto>> findChildFolders(@Parameter(description = "ID of the parent folder") @PathVariable Long parentId) {
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
    @Operation(summary = "Create a new folder", description = "Creates a new folder with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Folder successfully created",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<FolderResponseDto> createFolder(
            @Parameter(description = "Folder data to create", required = true) 
            @RequestBody FolderRequestDto folderDto) {
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
    @Operation(summary = "Update a folder", description = "Updates an existing folder with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Folder successfully updated",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FolderResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Folder not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<FolderResponseDto> updateFolder(
            @Parameter(description = "ID of the folder to update") @PathVariable Long id,
            @Parameter(description = "Updated folder data", required = true) @RequestBody FolderRequestDto folderDto) {
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
    @Operation(summary = "Delete a folder", description = "Deletes a folder by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Folder successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Folder not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@Parameter(description = "ID of the folder to delete") @PathVariable Long id) {
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
    @Operation(summary = "Add a feature to a folder", description = "Creates a new feature and adds it to the specified folder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feature successfully added to folder",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Folder not found",
                content = @Content)
    })
    @PostMapping("/{folderId}/features")
    public ResponseEntity<FeatureResponseDto> addFeatureToFolder(
            @Parameter(description = "ID of the folder to add the feature to") @PathVariable Long folderId,
            @Parameter(description = "Feature data to create", required = true) @RequestBody FeatureRequestDto featureDto) {
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
    @Operation(summary = "Move a feature to a different folder", description = "Moves an existing feature to the specified folder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature successfully moved to folder",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FeatureResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Folder or feature not found",
                content = @Content)
    })
    @PutMapping("/{folderId}/features/{featureId}")
    public ResponseEntity<FeatureResponseDto> moveFeatureToFolder(
            @Parameter(description = "ID of the folder to move the feature to") @PathVariable Long folderId,
            @Parameter(description = "ID of the feature to move") @PathVariable Long featureId) {
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
