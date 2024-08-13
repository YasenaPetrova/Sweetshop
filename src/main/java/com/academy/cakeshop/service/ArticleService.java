package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.ArticleRequestDTO;
import com.academy.cakeshop.persistance.entity.Article;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.repository.ArticleRepository;
import com.academy.cakeshop.persistance.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private static final Logger logger = Logger.getLogger(ArticleService.class.getName());

    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Article createArticle(ArticleRequestDTO articleRequestDTO) {
        logger.log(Level.INFO, "Creating article with name: " + articleRequestDTO.articleName());

        Article article = new Article();
        article.setArticleName(articleRequestDTO.articleName());
        article.setPrice(articleRequestDTO.price());

        Optional<Product> productOptional = productRepository.findById(articleRequestDTO.productId());
        if (productOptional.isPresent()) {
            article.setProduct(productOptional.get());
            Article savedArticle = articleRepository.save(article);
            logger.log(Level.INFO, "Article created successfully with ID: " + savedArticle.getId());
            return savedArticle;
        } else {
            logger.log(Level.SEVERE, "Product not found for ID: " + articleRequestDTO.productId());
            throw new RuntimeException("Product not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        logger.log(Level.INFO, "Fetching all articles");
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Article> getArticleById(Long id) {
        logger.log(Level.INFO, "Fetching article by ID: " + id);
        return articleRepository.findById(id);
    }

    @Transactional
    public Article updateArticle(Long id, ArticleRequestDTO articleRequestDTO) {
        logger.log(Level.INFO, "Updating article with ID: " + id);

        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            article.setArticleName(articleRequestDTO.articleName());
            article.setPrice(articleRequestDTO.price());

            Optional<Product> productOptional = productRepository.findById(articleRequestDTO.productId());
            if (productOptional.isPresent()) {
                article.setProduct(productOptional.get());
                Article updatedArticle = articleRepository.save(article);
                logger.log(Level.INFO, "Article updated successfully with ID: " + updatedArticle.getId());
                return updatedArticle;
            } else {
                logger.log(Level.SEVERE, "Product not found for ID: " + articleRequestDTO.productId());
                throw new RuntimeException("Product not found");
            }
        } else {
            logger.log(Level.SEVERE, "Article not found for ID: " + id);
            throw new RuntimeException("Article not found");
        }
    }

    @Transactional
    public void deleteArticle(Long id) {
        logger.log(Level.INFO, "Deleting article with ID: " + id);

        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            articleRepository.deleteById(id);
            logger.log(Level.INFO, "Article deleted successfully");
        } else {
            logger.log(Level.SEVERE, "Article not found for ID: " + id);
            throw new RuntimeException("Article not found");
        }
    }
}
