package com.funa.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Folder entity.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Find a folder by name.
     *
     * @param name The name to search for
     * @return The folder if found
     */
    Optional<Folder> findByName(String name);

    /**
     * Find folders with names containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching folders
     */
    List<Folder> findByNameContaining(String name);

    /**
     * Find folders by parent folder.
     *
     * @param parent The parent folder to search in
     * @return List of child folders
     */
    List<Folder> findByParent(Folder parent);

    /**
     * Find folders by parent folder ID.
     *
     * @param parentId The parent folder ID to search in
     * @return List of child folders
     */
    List<Folder> findByParentId(Long parentId);

    /**
     * Find root folders (folders with no parent).
     *
     * @return List of root folders
     */
    List<Folder> findByParentIsNull();
}