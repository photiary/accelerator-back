package com.funa.templateprompt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for managing template prompts.
 */
@Service
@Transactional
public class TemplatePromptService {

    private final TemplatePromptRepository templatePromptRepository;

    @Autowired
    public TemplatePromptService(TemplatePromptRepository templatePromptRepository) {
        this.templatePromptRepository = templatePromptRepository;
    }

    /**
     * Get all template prompts.
     *
     * @return List of all template prompts
     */
    @Transactional(readOnly = true)
    public List<TemplatePrompt> getAllTemplatePrompts() {
        return templatePromptRepository.findAll();
    }

    /**
     * Get a template prompt by ID.
     *
     * @param id The ID of the template prompt
     * @return The template prompt
     * @throws NoSuchElementException if the template prompt is not found
     */
    @Transactional(readOnly = true)
    public TemplatePrompt getTemplatePromptById(Long id) {
        return templatePromptRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Template prompt not found with ID: " + id));
    }

    /**
     * Find template prompts by name containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching template prompts
     */
    @Transactional(readOnly = true)
    public List<TemplatePrompt> findTemplatePromptsByName(String name) {
        return templatePromptRepository.findByNameContaining(name);
    }

    /**
     * Create a new template prompt.
     *
     * @param templatePrompt The template prompt to create
     * @return The created template prompt
     */
    public TemplatePrompt createTemplatePrompt(TemplatePrompt templatePrompt) {
        return templatePromptRepository.save(templatePrompt);
    }

    /**
     * Update an existing template prompt.
     *
     * @param id The ID of the template prompt to update
     * @param templatePrompt The updated template prompt data
     * @return The updated template prompt
     * @throws NoSuchElementException if the template prompt is not found
     */
    public TemplatePrompt updateTemplatePrompt(Long id, TemplatePrompt templatePrompt) {
        TemplatePrompt existingTemplatePrompt = getTemplatePromptById(id);
        existingTemplatePrompt.setName(templatePrompt.getName());
        existingTemplatePrompt.setPromptContent(templatePrompt.getPromptContent());
        return templatePromptRepository.save(existingTemplatePrompt);
    }

    /**
     * Delete a template prompt by ID.
     *
     * @param id The ID of the template prompt to delete
     * @throws NoSuchElementException if the template prompt is not found
     */
    public void deleteTemplatePrompt(Long id) {
        if (!templatePromptRepository.existsById(id)) {
            throw new NoSuchElementException("Template prompt not found with ID: " + id);
        }
        templatePromptRepository.deleteById(id);
    }
}