package io.squid.CyStation.service;

import io.squid.CyStation.model.Article;
import io.squid.CyStation.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getLatestArticles() {
        return articleRepository.findTop6ByOrderByDateDesc();
    }

    public List<Article> searchArticles(String keyword) {
        return articleRepository.findByTitleContainingIgnoreCase(keyword);
    }
}