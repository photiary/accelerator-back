package com.funa.folder;

import com.funa.feature.Feature;
import com.funa.feature.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for managing folders.
 */
@Service
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;
    private final FeatureRepository featureRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository, FeatureRepository featureRepository) {
        this.folderRepository = folderRepository;
        this.featureRepository = featureRepository;
    }

    /**
     * Get all folders.
     *
     * @return List of all folders
     */
    @Transactional(readOnly = true)
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    /**
     * Get all root folders (folders with no parent).
     *
     * @return List of root folders
     */
    @Transactional(readOnly = true)
    public List<Folder> getRootFolders() {
        return folderRepository.findByParentIsNull();
    }

    /**
     * Get a folder by ID.
     *
     * @param id The ID of the folder
     * @return The folder
     * @throws NoSuchElementException if the folder is not found
     */
    @Transactional(readOnly = true)
    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + id));
    }

    /**
     * Find folders by name containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching folders
     */
    @Transactional(readOnly = true)
    public List<Folder> findFoldersByName(String name) {
        return folderRepository.findByNameContaining(name);
    }

    /**
     * Find child folders of a parent folder.
     *
     * @param parentId The ID of the parent folder
     * @return List of child folders
     * @throws NoSuchElementException if the parent folder is not found
     */
    @Transactional(readOnly = true)
    public List<Folder> findChildFolders(Long parentId) {
        Folder parent = getFolderById(parentId);
        return folderRepository.findByParent(parent);
    }

    /**
     * Create a new folder.
     *
     * @param folder The folder to create
     * @param parentId The ID of the parent folder (optional, can be null for root folders)
     * @return The created folder
     * @throws NoSuchElementException if the parent folder is not found
     */
    public Folder createFolder(Folder folder, Long parentId) {
        if (parentId != null) {
            Folder parent = getFolderById(parentId);
            folder.setParent(parent);
        }
        return folderRepository.save(folder);
    }

    /**
     * Update an existing folder.
     *
     * @param id The ID of the folder to update
     * @param folder The updated folder data
     * @param parentId The ID of the parent folder (optional, can be null for root folders)
     * @return The updated folder
     * @throws NoSuchElementException if the folder or parent folder is not found
     * @throws IllegalArgumentException if trying to set a folder as its own parent or descendant
     */
    public Folder updateFolder(Long id, Folder folder, Long parentId) {
        Folder existingFolder = getFolderById(id);
        existingFolder.setName(folder.getName());
        existingFolder.setDescription(folder.getDescription());
        
        // Update parent folder
        if (parentId != null) {
            if (parentId.equals(id)) {
                throw new IllegalArgumentException("A folder cannot be its own parent");
            }
            
            // Check if the new parent is not a descendant of this folder
            if (isDescendant(id, parentId)) {
                throw new IllegalArgumentException("Cannot set a descendant folder as parent");
            }
            
            Folder parent = getFolderById(parentId);
            existingFolder.setParent(parent);
        } else {
            existingFolder.setParent(null);
        }
        
        return folderRepository.save(existingFolder);
    }

    /**
     * Check if a folder is a descendant of another folder.
     *
     * @param ancestorId The ID of the potential ancestor folder
     * @param descendantId The ID of the potential descendant folder
     * @return true if descendantId is a descendant of ancestorId, false otherwise
     */
    private boolean isDescendant(Long ancestorId, Long descendantId) {
        Folder descendant = getFolderById(descendantId);
        Folder parent = descendant.getParent();
        
        while (parent != null) {
            if (parent.getId().equals(ancestorId)) {
                return true;
            }
            parent = parent.getParent();
        }
        
        return false;
    }

    /**
     * Delete a folder by ID.
     * This will also delete all child folders and features in this folder.
     *
     * @param id The ID of the folder to delete
     * @throws NoSuchElementException if the folder is not found
     */
    public void deleteFolder(Long id) {
        if (!folderRepository.existsById(id)) {
            throw new NoSuchElementException("Folder not found with ID: " + id);
        }
        folderRepository.deleteById(id);
    }

    /**
     * Add a feature to a folder.
     *
     * @param folderId The ID of the folder
     * @param feature The feature to add
     * @return The added feature
     * @throws NoSuchElementException if the folder is not found
     */
    public Feature addFeatureToFolder(Long folderId, Feature feature) {
        Folder folder = getFolderById(folderId);
        feature.setFolder(folder);
        return featureRepository.save(feature);
    }

    /**
     * Move a feature to a different folder.
     *
     * @param featureId The ID of the feature to move
     * @param newFolderId The ID of the new folder
     * @return The moved feature
     * @throws NoSuchElementException if the feature or folder is not found
     */
    public Feature moveFeatureToFolder(Long featureId, Long newFolderId) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new NoSuchElementException("Feature not found with ID: " + featureId));
        
        Folder newFolder = getFolderById(newFolderId);
        feature.setFolder(newFolder);
        
        return featureRepository.save(feature);
    }
}