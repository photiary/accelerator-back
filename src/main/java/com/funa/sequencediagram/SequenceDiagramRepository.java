package com.funa.sequencediagram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SequenceDiagram entity.
 */
@Repository
public interface SequenceDiagramRepository extends JpaRepository<SequenceDiagram, Long> {

    /**
     * Find a sequence diagram by name.
     *
     * @param name The name to search for
     * @return The sequence diagram if found
     */
    Optional<SequenceDiagram> findByName(String name);

    /**
     * Find sequence diagrams with names containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching sequence diagrams
     */
    List<SequenceDiagram> findByNameContaining(String name);
}