package com.funa.folder;

import com.funa.common.entity.BaseEntity;
import com.funa.feature.Feature;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Folder entity for managing features.
 * Folders can contain other folders and features.
 */
@Entity
@Table(name = "tb_folder")
@Getter
@Setter
@NoArgsConstructor
public class Folder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Folder parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feature> features = new ArrayList<>();

    /**
     * Add a child folder to this folder.
     *
     * @param folder The folder to add
     */
    public void addFolder(Folder folder) {
        folders.add(folder);
        folder.setParent(this);
    }

    /**
     * Remove a child folder from this folder.
     *
     * @param folder The folder to remove
     */
    public void removeFolder(Folder folder) {
        folders.remove(folder);
        folder.setParent(null);
    }

    /**
     * Add a feature to this folder.
     *
     * @param feature The feature to add
     */
    public void addFeature(Feature feature) {
        features.add(feature);
        feature.setFolder(this);
    }

    /**
     * Remove a feature from this folder.
     *
     * @param feature The feature to remove
     */
    public void removeFeature(Feature feature) {
        features.remove(feature);
        feature.setFolder(null);
    }
}