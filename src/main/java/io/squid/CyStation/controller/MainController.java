package io.squid.CyStation.controller;

import io.squid.CyStation.model.Article;
import io.squid.CyStation.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@Controller
public class MainController {

    private final ArticleService articleService;

    public MainController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("latestArticles", articleService.getLatestArticles());
        return "public/index";
    }

    @GetMapping("/about")
    public String about() {
        return "public/about";
    }

    // Affiche le formulaire de création
    @GetMapping("/article/add")
    public String showAddArticleForm() {
        return "public/add-article";
    }

    // Enregistre l'article dans la base de données
    @PostMapping("/article/add")
    public String saveArticle(@RequestParam String title,
                              @RequestParam String subtitle,
                              @RequestParam String articleText) {
        Article article = new Article();
        article.setTitle(title);
        article.setSubtitle(subtitle);
        article.setArticleText(articleText);
        article.setDate(LocalDateTime.now()); // Définit la date actuelle

        articleService.saveArticle(article);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("articles", articleService.searchArticles(keyword));
        }
        model.addAttribute("keyword", keyword);
        return "public/search";
    }

    @GetMapping("/article/{id}")
    public String showArticleDetail(@PathVariable Long id, Model model) {
        // On récupère l'article via le service
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "public/article-detail";
    }
}