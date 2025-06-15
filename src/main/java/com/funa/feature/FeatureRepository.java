package com.funa.feature;

import com.funa.folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Feature entity.
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

    /**
     * Find a feature by name.
     *
     * @param name The name to search for
     * @return The feature if found
     */
    Optional<Feature> findByName(String name);

    /**
     * Find features with names containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching features
     */
    List<Feature> findByNameContaining(String name);

    /**
     * Find features by folder.
     *
     * @param folder The folder to search in
     * @return List of features in the folder
     */
    List<Feature> findByFolder(Folder folder);

    /**
     * Find features by folder ID.
     *
     * @param folderId The folder ID to search in
     * @return List of features in the folder
     */
    List<Feature> findByFolderId(Long folderId);
}