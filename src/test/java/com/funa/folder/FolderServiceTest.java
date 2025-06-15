package com.funa.folder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FolderServiceTest {

    @Autowired
    private FolderService folderService;

    @Test
    public void testCreateAndGetFolder() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Test Folder");
        folder.setDescription("Test Description");

        Folder createdFolder = folderService.createFolder(folder, null);

        // Verify the folder was created
        assertNotNull(createdFolder.getId());
        assertEquals("Test Folder", createdFolder.getName());
        assertEquals("Test Description", createdFolder.getDescription());
        assertNull(createdFolder.getParent());

        // Get the folder by ID
        Folder retrievedFolder = folderService.getFolderById(createdFolder.getId());
        assertEquals(createdFolder.getId(), retrievedFolder.getId());
        assertEquals("Test Folder", retrievedFolder.getName());
    }

    @Test
    public void testCreateNestedFolders() {
        // Create a parent folder
        Folder parentFolder = new Folder();
        parentFolder.setName("Parent Folder");
        parentFolder.setDescription("Parent Description");
        Folder createdParentFolder = folderService.createFolder(parentFolder, null);

        // Create a child folder
        Folder childFolder = new Folder();
        childFolder.setName("Child Folder");
        childFolder.setDescription("Child Description");
        Folder createdChildFolder = folderService.createFolder(childFolder, createdParentFolder.getId());

        // Verify the child folder has the correct parent
        assertNotNull(createdChildFolder.getParent());
        assertEquals(createdParentFolder.getId(), createdChildFolder.getParent().getId());

        // Get child folders of the parent
        List<Folder> childFolders = folderService.findChildFolders(createdParentFolder.getId());
        assertEquals(1, childFolders.size());
        assertEquals(createdChildFolder.getId(), childFolders.get(0).getId());
    }

    @Test
    public void testUpdateFolder() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Original Name");
        folder.setDescription("Original Description");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Update the folder
        Folder updatedData = new Folder();
        updatedData.setName("Updated Name");
        updatedData.setDescription("Updated Description");
        Folder updatedFolder = folderService.updateFolder(createdFolder.getId(), updatedData, null);

        // Verify the folder was updated
        assertEquals(createdFolder.getId(), updatedFolder.getId());
        assertEquals("Updated Name", updatedFolder.getName());
        assertEquals("Updated Description", updatedFolder.getDescription());
    }

    @Test
    public void testDeleteFolder() {
        // Create a folder
        Folder folder = new Folder();
        folder.setName("Folder to Delete");
        Folder createdFolder = folderService.createFolder(folder, null);

        // Delete the folder
        folderService.deleteFolder(createdFolder.getId());

        // Verify the folder was deleted
        assertThrows(NoSuchElementException.class, () -> {
            folderService.getFolderById(createdFolder.getId());
        });
    }

    @Test
    public void testFindFoldersByName() {
        // Create folders with similar names
        Folder folder1 = new Folder();
        folder1.setName("Test Folder One");
        folderService.createFolder(folder1, null);

        Folder folder2 = new Folder();
        folder2.setName("Test Folder Two");
        folderService.createFolder(folder2, null);

        Folder folder3 = new Folder();
        folder3.setName("Another Folder");
        folderService.createFolder(folder3, null);

        // Search for folders by name
        List<Folder> foundFolders = folderService.findFoldersByName("Test");
        assertEquals(2, foundFolders.size());

        List<Folder> anotherFolders = folderService.findFoldersByName("Another");
        assertEquals(1, anotherFolders.size());
    }
}