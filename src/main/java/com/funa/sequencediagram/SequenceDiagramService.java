package com.funa.sequencediagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service for managing sequence diagrams.
 */
@Service
@Transactional
public class SequenceDiagramService {

    private final SequenceDiagramRepository sequenceDiagramRepository;

    @Autowired
    public SequenceDiagramService(SequenceDiagramRepository sequenceDiagramRepository) {
        this.sequenceDiagramRepository = sequenceDiagramRepository;
    }

    /**
     * Get all sequence diagrams.
     *
     * @return List of all sequence diagrams
     */
    @Transactional(readOnly = true)
    public List<SequenceDiagram> getAllSequenceDiagrams() {
        return sequenceDiagramRepository.findAll();
    }

    /**
     * Get a sequence diagram by ID.
     *
     * @param id The ID of the sequence diagram
     * @return The sequence diagram
     * @throws NoSuchElementException if the sequence diagram is not found
     */
    @Transactional(readOnly = true)
    public SequenceDiagram getSequenceDiagramById(Long id) {
        return sequenceDiagramRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sequence diagram not found with ID: " + id));
    }

    /**
     * Find sequence diagrams by name containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching sequence diagrams
     */
    @Transactional(readOnly = true)
    public List<SequenceDiagram> findSequenceDiagramsByName(String name) {
        return sequenceDiagramRepository.findByNameContaining(name);
    }

    /**
     * Create a new sequence diagram.
     *
     * @param sequenceDiagram The sequence diagram to create
     * @return The created sequence diagram
     */
    public SequenceDiagram createSequenceDiagram(SequenceDiagram sequenceDiagram) {
        return sequenceDiagramRepository.save(sequenceDiagram);
    }

    /**
     * Update an existing sequence diagram.
     *
     * @param id The ID of the sequence diagram to update
     * @param sequenceDiagram The updated sequence diagram data
     * @return The updated sequence diagram
     * @throws NoSuchElementException if the sequence diagram is not found
     */
    public SequenceDiagram updateSequenceDiagram(Long id, SequenceDiagram sequenceDiagram) {
        SequenceDiagram existingSequenceDiagram = getSequenceDiagramById(id);
        existingSequenceDiagram.setName(sequenceDiagram.getName());
        existingSequenceDiagram.setSequenceDiagramContent(sequenceDiagram.getSequenceDiagramContent());
        return sequenceDiagramRepository.save(existingSequenceDiagram);
    }

    /**
     * Delete a sequence diagram by ID.
     *
     * @param id The ID of the sequence diagram to delete
     * @throws NoSuchElementException if the sequence diagram is not found
     */
    public void deleteSequenceDiagram(Long id) {
        if (!sequenceDiagramRepository.existsById(id)) {
            throw new NoSuchElementException("Sequence diagram not found with ID: " + id);
        }
        sequenceDiagramRepository.deleteById(id);
    }
}