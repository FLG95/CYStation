package io.squid.CyStation.repository;

import io.squid.CyStation.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findTop6ByOrderByDateDesc();

    List<Article> findByTitleContainingIgnoreCase(String keyword);
}