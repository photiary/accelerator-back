package com.funa.feature;

import com.funa.folder.Folder;
import com.funa.folder.FolderService;
import com.funa.sequencediagram.SequenceDiagram;
import com.funa.sequencediagram.SequenceDiagramService;
import com.funa.sqlquery.SqlQuery;
import com.funa.sqlquery.SqlQueryService;
import com.funa.templateprompt.TemplatePrompt;
import com.funa.templateprompt.TemplatePromptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FeatureServiceTest {

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private TemplatePromptService templatePromptService;

    @Autowired
    private SequenceDiagramService sequenceDiagramService;

    @Autowired
    private SqlQueryService sqlQueryService;

    @Test
    public void testCreateAndGetFeature() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Create a feature
        Feature feature = new Feature();
        feature.setName("Test Feature");
        feature.setDescription("Test Description");

        Feature createdFeature = featureService.createFeature(
                feature, createdFolder.getId(), null, null, null);

        // Verify the feature was created
        assertNotNull(createdFeature.getId());
        assertEquals("Test Feature", createdFeature.getName());
        assertEquals("Test Description", createdFeature.getDescription());
        assertNotNull(createdFeature.getFolder());
        assertEquals(createdFolder.getId(), createdFeature.getFolder().getId());

        // Get the feature by ID
        Feature retrievedFeature = featureService.getFeatureById(createdFeature.getId());
        assertEquals(createdFeature.getId(), retrievedFeature.getId());
        assertEquals("Test Feature", retrievedFeature.getName());
    }

    @Test
    public void testCreateFeatureWithAllRelationships() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Create a template prompt
        TemplatePrompt templatePrompt = new TemplatePrompt();
        templatePrompt.setName("Test Template Prompt");
        templatePrompt.setPromptContent("Test prompt content");
        TemplatePrompt createdTemplatePrompt = templatePromptService.createTemplatePrompt(templatePrompt);

        // Create a sequence diagram
        SequenceDiagram sequenceDiagram = new SequenceDiagram();
        sequenceDiagram.setName("Test Sequence Diagram");
        sequenceDiagram.setSequenceDiagramContent("Test sequence diagram content");
        SequenceDiagram createdSequenceDiagram = sequenceDiagramService.createSequenceDiagram(sequenceDiagram);

        // Create a SQL query
        SqlQuery sqlQuery = new SqlQuery();
        sqlQuery.setName("Test SQL Query");
        sqlQuery.setQueryContent("SELECT * FROM test");
        SqlQuery createdSqlQuery = sqlQueryService.createSqlQuery(sqlQuery);

        // Create a feature with all relationships
        Feature feature = new Feature();
        feature.setName("Test Feature");
        feature.setDescription("Test Description");

        Feature createdFeature = featureService.createFeature(
                feature,
                createdFolder.getId(),
                createdTemplatePrompt.getId(),
                createdSequenceDiagram.getId(),
                createdSqlQuery.getId());

        // Verify the feature was created with all relationships
        assertNotNull(createdFeature.getId());
        assertEquals("Test Feature", createdFeature.getName());
        assertEquals("Test Description", createdFeature.getDescription());
        
        assertNotNull(createdFeature.getFolder());
        assertEquals(createdFolder.getId(), createdFeature.getFolder().getId());
        
        assertNotNull(createdFeature.getTemplatePrompt());
        assertEquals(createdTemplatePrompt.getId(), createdFeature.getTemplatePrompt().getId());
        
        assertNotNull(createdFeature.getSequenceDiagram());
        assertEquals(createdSequenceDiagram.getId(), createdFeature.getSequenceDiagram().getId());
        
        assertNotNull(createdFeature.getSqlQuery());
        assertEquals(createdSqlQuery.getId(), createdFeature.getSqlQuery().getId());
    }

    @Test
    public void testUpdateFeature() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Create a feature
        Feature feature = new Feature();
        feature.setName("Original Name");
        feature.setDescription("Original Description");
        Feature createdFeature = featureService.createFeature(feature, createdFolder.getId(), null, null, null);

        // Create a new folder for moving the feature
        Folder newFolder = new Folder();
        newFolder.setName("New Folder");
        Folder createdNewFolder = folderService.createFolder(newFolder, null);

        // Update the feature
        Feature updatedData = new Feature();
        updatedData.setName("Updated Name");
        updatedData.setDescription("Updated Description");
        Feature updatedFeature = featureService.updateFeature(
                createdFeature.getId(),
                updatedData,
                createdNewFolder.getId(),
                null,
                null,
                null);

        // Verify the feature was updated
        assertEquals(createdFeature.getId(), updatedFeature.getId());
        assertEquals("Updated Name", updatedFeature.getName());
        assertEquals("Updated Description", updatedFeature.getDescription());
        assertEquals(createdNewFolder.getId(), updatedFeature.getFolder().getId());
    }

    @Test
    public void testDeleteFeature() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Create a feature
        Feature feature = new Feature();
        feature.setName("Feature to Delete");
        Feature createdFeature = featureService.createFeature(feature, createdFolder.getId(), null, null, null);

        // Delete the feature
        featureService.deleteFeature(createdFeature.getId());

        // Verify the feature was deleted
        assertThrows(NoSuchElementException.class, () -> {
            featureService.getFeatureById(createdFeature.getId());
        });
    }

    @Test
    public void testFindFeaturesByFolder() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Create features in the folder
        Feature feature1 = new Feature();
        feature1.setName("Feature One");
        featureService.createFeature(feature1, createdFolder.getId(), null, null, null);

        Feature feature2 = new Feature();
        feature2.setName("Feature Two");
        featureService.createFeature(feature2, createdFolder.getId(), null, null, null);

        // Create another folder with a feature
        Folder anotherFolder = new Folder();
        anotherFolder.setName("Another Folder");
        Folder createdAnotherFolder = folderService.createFolder(anotherFolder, null);

        Feature feature3 = new Feature();
        feature3.setName("Feature Three");
        featureService.createFeature(feature3, createdAnotherFolder.getId(), null, null, null);

        // Find features by folder
        List<Feature> featuresInFolder = featureService.findFeaturesByFolder(createdFolder.getId());
        assertEquals(2, featuresInFolder.size());

        List<Feature> featuresInAnotherFolder = featureService.findFeaturesByFolder(createdAnotherFolder.getId());
        assertEquals(1, featuresInAnotherFolder.size());
    }
}