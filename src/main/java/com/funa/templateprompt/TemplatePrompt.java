package com.funa.templateprompt;

import com.funa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TemplatePrompt entity for managing prompt templates.
 */
@Entity
@Table(name = "tb_template_prompt")
@Getter
@Setter
@NoArgsConstructor
public class TemplatePrompt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "prompt_content", columnDefinition = "TEXT", nullable = false)
    private String promptContent;
}