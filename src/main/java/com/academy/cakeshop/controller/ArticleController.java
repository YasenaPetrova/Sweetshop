package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.ArticleRequestDTO;
import com.academy.cakeshop.persistance.entity.Article;
import com.academy.cakeshop.service.ArticleService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;


    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Optional<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping
    public Article createArticle(@RequestBody ArticleRequestDTO articleRequestDTO) {
        return articleService.createArticle(articleRequestDTO);
    }

    @PutMapping("/{id}")
    public Article updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDTO articleRequestDTO) {
        return articleService.updateArticle(id, articleRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}

