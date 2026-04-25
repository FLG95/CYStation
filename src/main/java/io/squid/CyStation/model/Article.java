package io.squid.CyStation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    private String type; // ← NOUVEAU

    public Article() {
    }

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

    public String getType() { return type; }           // ← NOUVEAU
    public void setType(String type) { this.type = type; } // ← NOUVEAU
}