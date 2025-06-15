package com.funa.templateprompt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TemplatePrompt entity.
 */
@Repository
public interface TemplatePromptRepository extends JpaRepository<TemplatePrompt, Long> {

    /**
     * Find a template prompt by name.
     *
     * @param name The name to search for
     * @return The template prompt if found
     */
    Optional<TemplatePrompt> findByName(String name);

    /**
     * Find template prompts with names containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching template prompts
     */
    List<TemplatePrompt> findByNameContaining(String name);
}