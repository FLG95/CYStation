package io.squid.CyStation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subtitle;

    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String articleText;

    private String imageUrl;

    // Stocké en base comme "espace,nature,urgent"
    private String types;

    public Article() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getArticleText() { return articleText; }
    public void setArticleText(String articleText) { this.articleText = articleText; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Getter/Setter raw pour JPA (la string "espace,nature")
    public String getTypes() { return types; }
    public void setTypes(String types) { this.types = types; }

    // Utilitaire : convertit la string en List pour Thymeleaf
    public List<String> getTypeList() {
        if (types == null || types.isEmpty()) return List.of();
        return Arrays.asList(types.split(","));
    }
}