package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.ArticleRequestDTO;
import com.academy.cakeshop.persistance.entity.Article;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.repository.ArticleRepository;
import com.academy.cakeshop.persistance.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateArticle_Success() {
        ArticleRequestDTO requestDTO = new ArticleRequestDTO("Cake", 10.0, 1L);
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> {
            Article article = invocation.getArgument(0);
            article.setId(1L); // Set an ID for the saved article
            return article;
        });

        Article article = articleService.createArticle(requestDTO);

        assertNotNull(article);
        assertEquals("Cake", article.getArticleName());
        assertEquals(10.0, article.getPrice());
        assertEquals(product.getId(), article.getProduct().getId());
        verify(productRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testCreateArticle_ProductNotFound() {
        ArticleRequestDTO requestDTO = new ArticleRequestDTO("Cake", 10.0, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.createArticle(requestDTO);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(articleRepository, times(0)).save(any(Article.class));
    }

    @Test
    void testGetAllArticles() {
        List<Article> articles = List.of(new Article(), new Article());

        when(articleRepository.findAll()).thenReturn(articles);

        List<Article> result = articleService.getAllArticles();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void testGetArticleById() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.getArticleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateArticle_Success() {
        Article existingArticle = new Article();
        existingArticle.setId(1L);

        ArticleRequestDTO requestDTO = new ArticleRequestDTO("Cookie", 5.0, 2L);
        Product product = new Product();
        product.setId(2L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Article updatedArticle = articleService.updateArticle(1L, requestDTO);

        assertNotNull(updatedArticle);
        assertEquals("Cookie", updatedArticle.getArticleName());
        assertEquals(5.0, updatedArticle.getPrice());
        assertEquals(product.getId(), updatedArticle.getProduct().getId());
        verify(articleRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testUpdateArticle_ArticleNotFound() {
        ArticleRequestDTO requestDTO = new ArticleRequestDTO("Cookie", 5.0, 2L);

        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.updateArticle(1L, requestDTO);
        });

        assertEquals("Article not found", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).findById(anyLong());
        verify(articleRepository, times(0)).save(any(Article.class));
    }

    @Test
    void testUpdateArticle_ProductNotFound() {
        Article existingArticle = new Article();
        existingArticle.setId(1L);

        ArticleRequestDTO requestDTO = new ArticleRequestDTO("Cookie", 5.0, 2L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.updateArticle(1L, requestDTO);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
        verify(articleRepository, times(0)).save(any(Article.class));
    }

    @Test
    void testDeleteArticle_Success() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        articleService.deleteArticle(1L);

        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteArticle_ArticleNotFound() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.deleteArticle(1L);
        });

        assertEquals("Article not found", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(0)).deleteById(anyLong());
    }
}
