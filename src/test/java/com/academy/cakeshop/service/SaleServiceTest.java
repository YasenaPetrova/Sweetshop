package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.SaleRequestDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.persistance.entity.Article;
import com.academy.cakeshop.persistance.entity.Sale;
import com.academy.cakeshop.persistance.repository.ArticleRepository;
import com.academy.cakeshop.persistance.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSale_Success() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        Long articleId = 1L;
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO(date, amount, articleId);
        Article article = new Article();
        article.setId(articleId);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(saleRepository.save(any(Sale.class))).thenReturn(new Sale());

        Sale sale = saleService.createSale(saleRequestDTO);

        assertNotNull(sale);
        verify(articleRepository, times(1)).findById(articleId);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void testCreateSale_ArticleNotFound() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        Long articleId = 1L;
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO(date, amount, articleId);

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            saleService.createSale(saleRequestDTO);
        });

        assertEquals("Sale not found", exception.getMessage());
        verify(articleRepository, times(1)).findById(articleId);
        verify(saleRepository, times(0)).save(any(Sale.class));
    }

    @Test
    void testGetSalesByDate() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        Long articleId = 1L;
        List<SaleResponseDTO> saleResponseDTOList = List.of(new SaleResponseDTO(date, amount, articleId));

        when(saleRepository.findBySaleDate(date)).thenReturn(saleResponseDTOList);

        List<SaleResponseDTO> result = saleService.getSalesByDate(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepository, times(1)).findBySaleDate(date);
    }

    @Test
    void testGetAllSale() {
        List<Sale> saleList = List.of(new Sale());

        when(saleRepository.findAll()).thenReturn(saleList);

        List<Sale> result = saleService.getAllSale();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void testGetSaleById() {
        Sale sale = new Sale();
        sale.setId(1L);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        Optional<Sale> result = saleService.getSaleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(saleRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateSale_Success() {
        Sale sale = new Sale();
        sale.setId(1L);
        Article article = new Article();
        article.setId(1L);
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        Long articleId = 1L;
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO(date, amount, articleId);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(saleRepository.save(any(Sale.class))).thenReturn(new Sale());

        Sale updatedSale = saleService.updateSale(1L, saleRequestDTO);

        assertNotNull(updatedSale);
        verify(saleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).findById(1L);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void testUpdateSale_SaleNotFound() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        Long articleId = 1L;
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO(date, amount, articleId);

        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            saleService.updateSale(1L, saleRequestDTO);
        });

        assertEquals("Sale not found", exception.getMessage());
        verify(saleRepository, times(1)).findById(1L);
        verify(articleRepository, times(0)).findById(anyLong());
        verify(saleRepository, times(0)).save(any(Sale.class));
    }

    @Test
    void testDeleteSale_Success() {
        Sale sale = new Sale();
        sale.setId(1L);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        saleService.deleteSale(1L);

        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSale_SaleNotFound() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            saleService.deleteSale(1L);
        });

        assertEquals("Sale not found", exception.getMessage());
        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, times(0)).deleteById(anyLong());
    }
}
