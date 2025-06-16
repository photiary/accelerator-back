package com.funa.feature;

import com.funa.folder.Folder;
import com.funa.folder.FolderRepository;
import com.funa.sequencediagram.SequenceDiagram;
import com.funa.sequencediagram.SequenceDiagramRepository;
import com.funa.sqlquery.SqlQuery;
import com.funa.sqlquery.SqlQueryRepository;
import com.funa.templateprompt.TemplatePrompt;
import com.funa.templateprompt.TemplatePromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for managing features.
 */
@Service
@Transactional
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final FolderRepository folderRepository;
    private final TemplatePromptRepository templatePromptRepository;
    private final SequenceDiagramRepository sequenceDiagramRepository;
    private final SqlQueryRepository sqlQueryRepository;

    @Autowired
    public FeatureService(
            FeatureRepository featureRepository,
            FolderRepository folderRepository,
            TemplatePromptRepository templatePromptRepository,
            SequenceDiagramRepository sequenceDiagramRepository,
            SqlQueryRepository sqlQueryRepository) {
        this.featureRepository = featureRepository;
        this.folderRepository = folderRepository;
        this.templatePromptRepository = templatePromptRepository;
        this.sequenceDiagramRepository = sequenceDiagramRepository;
        this.sqlQueryRepository = sqlQueryRepository;
    }

    /**
     * Get all features.
     *
     * @return List of all features
     */
    @Transactional(readOnly = true)
    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    /**
     * Get a feature by ID.
     *
     * @param id The ID of the feature
     * @return The feature
     * @throws NoSuchElementException if the feature is not found
     */
    @Transactional(readOnly = true)
    public Feature getFeatureById(Long id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Feature not found with ID: " + id));
    }

    /**
     * Find features by name containing the given text.
     *
     * @param name The name text to search for
     * @return List of matching features
     */
    @Transactional(readOnly = true)
    public List<Feature> findFeaturesByName(String name) {
        return featureRepository.findByNameContaining(name);
    }

    /**
     * Find features by folder.
     *
     * @param folderId The folder ID to search in
     * @return List of features in the folder
     * @throws NoSuchElementException if the folder is not found
     */
    @Transactional(readOnly = true)
    public List<Feature> findFeaturesByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + folderId));
        return featureRepository.findByFolder(folder);
    }

    /**
     * Create a new feature.
     *
     * @param feature The feature to create
     * @param folderId The ID of the folder to add the feature to
     * @param templatePromptId The ID of the template prompt to associate with the feature (optional)
     * @param sequenceDiagramName The name for a new sequence diagram to create (optional)
     * @param sequenceDiagramContent The content for a new sequence diagram to create (optional)
     * @param sqlQueryName The name for a new SQL query to create (optional)
     * @param sqlQueryContent The content for a new SQL query to create (optional)
     * @return The created feature
     * @throws NoSuchElementException if any of the referenced entities are not found
     */
    @Transactional
    public Feature createFeature(
            Feature feature,
            Long folderId,
            Long templatePromptId,
            String sequenceDiagramName,
            String sequenceDiagramContent,
            String sqlQueryName,
            String sqlQueryContent) {

        // Set folder
        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + folderId));
            feature.setFolder(folder);
        }

        // Set template prompt
        if (templatePromptId != null) {
            TemplatePrompt templatePrompt = templatePromptRepository.findById(templatePromptId)
                    .orElseThrow(() -> new NoSuchElementException("Template prompt not found with ID: " + templatePromptId));
            feature.setTemplatePrompt(templatePrompt);
        }

        // Create and set sequence diagram if name and content are provided
        if (sequenceDiagramName != null && !sequenceDiagramName.isEmpty() && 
            sequenceDiagramContent != null && !sequenceDiagramContent.isEmpty()) {
            SequenceDiagram sequenceDiagram = new SequenceDiagram();
            sequenceDiagram.setName(sequenceDiagramName);
            sequenceDiagram.setSequenceDiagramContent(sequenceDiagramContent);
            sequenceDiagram = sequenceDiagramRepository.save(sequenceDiagram);
            feature.setSequenceDiagram(sequenceDiagram);
        }

        // Create and set SQL query if name and content are provided
        if (sqlQueryName != null && !sqlQueryName.isEmpty() && 
            sqlQueryContent != null && !sqlQueryContent.isEmpty()) {
            SqlQuery sqlQuery = new SqlQuery();
            sqlQuery.setName(sqlQueryName);
            sqlQuery.setQueryContent(sqlQueryContent);
            sqlQuery = sqlQueryRepository.save(sqlQuery);
            feature.setSqlQuery(sqlQuery);
        }

        return featureRepository.save(feature);
    }

    /**
     * Create a new feature with existing related entities.
     *
     * @param feature The feature to create
     * @param folderId The ID of the folder to add the feature to
     * @param templatePromptId The ID of the template prompt to associate with the feature (optional)
     * @param sequenceDiagramId The ID of the sequence diagram to associate with the feature (optional)
     * @param sqlQueryId The ID of the SQL query to associate with the feature (optional)
     * @return The created feature
     * @throws NoSuchElementException if any of the referenced entities are not found
     */
    @Transactional
    public Feature createFeature(
            Feature feature,
            Long folderId,
            Long templatePromptId,
            Long sequenceDiagramId,
            Long sqlQueryId) {

        // Set folder
        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + folderId));
            feature.setFolder(folder);
        }

        // Set template prompt
        if (templatePromptId != null) {
            TemplatePrompt templatePrompt = templatePromptRepository.findById(templatePromptId)
                    .orElseThrow(() -> new NoSuchElementException("Template prompt not found with ID: " + templatePromptId));
            feature.setTemplatePrompt(templatePrompt);
        }

        // Set sequence diagram
        if (sequenceDiagramId != null) {
            SequenceDiagram sequenceDiagram = sequenceDiagramRepository.findById(sequenceDiagramId)
                    .orElseThrow(() -> new NoSuchElementException("Sequence diagram not found with ID: " + sequenceDiagramId));
            feature.setSequenceDiagram(sequenceDiagram);
        }

        // Set SQL query
        if (sqlQueryId != null) {
            SqlQuery sqlQuery = sqlQueryRepository.findById(sqlQueryId)
                    .orElseThrow(() -> new NoSuchElementException("SQL query not found with ID: " + sqlQueryId));
            feature.setSqlQuery(sqlQuery);
        }

        return featureRepository.save(feature);
    }

    /**
     * Update an existing feature.
     *
     * @param id The ID of the feature to update
     * @param feature The updated feature data
     * @param folderId The ID of the folder to move the feature to (optional)
     * @param templatePromptId The ID of the template prompt to associate with the feature (optional)
     * @param sequenceDiagramName The name for a new sequence diagram to create (optional)
     * @param sequenceDiagramContent The content for a new sequence diagram to create (optional)
     * @param sqlQueryName The name for a new SQL query to create (optional)
     * @param sqlQueryContent The content for a new SQL query to create (optional)
     * @return The updated feature
     * @throws NoSuchElementException if any of the referenced entities are not found
     */
    @Transactional
    public Feature updateFeature(
            Long id,
            Feature feature,
            Long folderId,
            Long templatePromptId,
            String sequenceDiagramName,
            String sequenceDiagramContent,
            String sqlQueryName,
            String sqlQueryContent) {

        Feature existingFeature = getFeatureById(id);
        existingFeature.setName(feature.getName());
        existingFeature.setDescription(feature.getDescription());

        // Update folder
        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + folderId));
            existingFeature.setFolder(folder);
        }

        // Update template prompt
        if (templatePromptId != null) {
            TemplatePrompt templatePrompt = templatePromptRepository.findById(templatePromptId)
                    .orElseThrow(() -> new NoSuchElementException("Template prompt not found with ID: " + templatePromptId));
            existingFeature.setTemplatePrompt(templatePrompt);
        } else if (templatePromptId == null && feature.getTemplatePrompt() == null) {
            existingFeature.setTemplatePrompt(null);
        }

        // Create and set sequence diagram if name and content are provided
        if (sequenceDiagramName != null && !sequenceDiagramName.isEmpty() && 
            sequenceDiagramContent != null && !sequenceDiagramContent.isEmpty()) {
            // If feature already has a sequence diagram, update it
            if (existingFeature.getSequenceDiagram() != null) {
                SequenceDiagram existingSequenceDiagram = existingFeature.getSequenceDiagram();
                existingSequenceDiagram.setName(sequenceDiagramName);
                existingSequenceDiagram.setSequenceDiagramContent(sequenceDiagramContent);
                sequenceDiagramRepository.save(existingSequenceDiagram);
            } else {
                // Create a new sequence diagram
                SequenceDiagram sequenceDiagram = new SequenceDiagram();
                sequenceDiagram.setName(sequenceDiagramName);
                sequenceDiagram.setSequenceDiagramContent(sequenceDiagramContent);
                sequenceDiagram = sequenceDiagramRepository.save(sequenceDiagram);
                existingFeature.setSequenceDiagram(sequenceDiagram);
            }
        }

        // Create and set SQL query if name and content are provided
        if (sqlQueryName != null && !sqlQueryName.isEmpty() && 
            sqlQueryContent != null && !sqlQueryContent.isEmpty()) {
            // If feature already has a SQL query, update it
            if (existingFeature.getSqlQuery() != null) {
                SqlQuery existingSqlQuery = existingFeature.getSqlQuery();
                existingSqlQuery.setName(sqlQueryName);
                existingSqlQuery.setQueryContent(sqlQueryContent);
                sqlQueryRepository.save(existingSqlQuery);
            } else {
                // Create a new SQL query
                SqlQuery sqlQuery = new SqlQuery();
                sqlQuery.setName(sqlQueryName);
                sqlQuery.setQueryContent(sqlQueryContent);
                sqlQuery = sqlQueryRepository.save(sqlQuery);
                existingFeature.setSqlQuery(sqlQuery);
            }
        }

        return featureRepository.save(existingFeature);
    }

    /**
     * Update an existing feature with existing related entities.
     *
     * @param id The ID of the feature to update
     * @param feature The updated feature data
     * @param folderId The ID of the folder to move the feature to (optional)
     * @param templatePromptId The ID of the template prompt to associate with the feature (optional)
     * @param sequenceDiagramId The ID of the sequence diagram to associate with the feature (optional)
     * @param sqlQueryId The ID of the SQL query to associate with the feature (optional)
     * @return The updated feature
     * @throws NoSuchElementException if any of the referenced entities are not found
     */
    @Transactional
    public Feature updateFeature(
            Long id,
            Feature feature,
            Long folderId,
            Long templatePromptId,
            Long sequenceDiagramId,
            Long sqlQueryId) {

        Feature existingFeature = getFeatureById(id);
        existingFeature.setName(feature.getName());
        existingFeature.setDescription(feature.getDescription());

        // Update folder
        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new NoSuchElementException("Folder not found with ID: " + folderId));
            existingFeature.setFolder(folder);
        }

        // Update template prompt
        if (templatePromptId != null) {
            TemplatePrompt templatePrompt = templatePromptRepository.findById(templatePromptId)
                    .orElseThrow(() -> new NoSuchElementException("Template prompt not found with ID: " + templatePromptId));
            existingFeature.setTemplatePrompt(templatePrompt);
        } else if (templatePromptId == null && feature.getTemplatePrompt() == null) {
            existingFeature.setTemplatePrompt(null);
        }

        // Update sequence diagram
        if (sequenceDiagramId != null) {
            SequenceDiagram sequenceDiagram = sequenceDiagramRepository.findById(sequenceDiagramId)
                    .orElseThrow(() -> new NoSuchElementException("Sequence diagram not found with ID: " + sequenceDiagramId));
            existingFeature.setSequenceDiagram(sequenceDiagram);
        } else if (sequenceDiagramId == null && feature.getSequenceDiagram() == null) {
            existingFeature.setSequenceDiagram(null);
        }

        // Update SQL query
        if (sqlQueryId != null) {
            SqlQuery sqlQuery = sqlQueryRepository.findById(sqlQueryId)
                    .orElseThrow(() -> new NoSuchElementException("SQL query not found with ID: " + sqlQueryId));
            existingFeature.setSqlQuery(sqlQuery);
        } else if (sqlQueryId == null && feature.getSqlQuery() == null) {
            existingFeature.setSqlQuery(null);
        }

        return featureRepository.save(existingFeature);
    }

    /**
     * Delete a feature by ID.
     *
     * @param id The ID of the feature to delete
     * @throws NoSuchElementException if the feature is not found
     */
    public void deleteFeature(Long id) {
        if (!featureRepository.existsById(id)) {
            throw new NoSuchElementException("Feature not found with ID: " + id);
        }
        featureRepository.deleteById(id);
    }
}
