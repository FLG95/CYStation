package io.squid.CyStation.controller;

import io.squid.CyStation.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("articles", articleService.searchArticles(keyword));
        }
        model.addAttribute("keyword", keyword);
        return "public/search";
    }
}