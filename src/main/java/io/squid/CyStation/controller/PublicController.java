package io.squid.CyStation.controller;

import io.squid.CyStation.model.Article;
import io.squid.CyStation.repository.ZoneRepository;
import io.squid.CyStation.service.ArticleService;
import io.squid.CyStation.service.ZoneService;
import org.springframework.stereotype.Controller;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@Controller
public class MainController {

    private final ArticleService articleService;
    private final ZoneRepository zoneRepository;

    public MainController(ArticleService articleService, ZoneRepository zoneRepository) {
        this.articleService = articleService;
        this.zoneRepository = zoneRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("latestArticles", articleService.getLatestArticles());
        model.addAttribute("zones", zoneRepository.findAll());
        return "public/index";
    }

    @GetMapping("/about")
    public String about() {
        return "public/about";
    }

    @GetMapping("/articles")
    public String showAllArticles(Model model) {
        model.addAttribute("allArticles", articleService.findAll());
        return "public/articles";
    }

    @GetMapping("/article/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "public/add-article";
    }

    @PostMapping("/article/add")
    public String saveArticle(@ModelAttribute Article article,
                              @RequestParam(value = "types", required = false) List<String> types) {
        article.setDate(LocalDateTime.now());
        article.setTypes(types != null ? String.join(",", types) : "");
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
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "public/article-detail";
    }
}