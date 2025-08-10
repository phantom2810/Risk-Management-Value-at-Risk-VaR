package com.riskmanagement.varcalculator.service;

import com.riskmanagement.varcalculator.dto.request.CreatePortfolioRequest;
import com.riskmanagement.varcalculator.dto.response.PortfolioResponse;
import com.riskmanagement.varcalculator.entity.Portfolio;
import com.riskmanagement.varcalculator.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private CreatePortfolioRequest createPortfolioRequest;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        createPortfolioRequest = new CreatePortfolioRequest();
        createPortfolioRequest.setName("Test Portfolio");
        createPortfolioRequest.setDescription("Test Description");
        createPortfolioRequest.setBaseCurrency("USD");

        portfolio = Portfolio.builder()
                .id(1L)
                .name("Test Portfolio")
                .description("Test Description")
                .baseCurrency("USD")
                .status(Portfolio.PortfolioStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createPortfolio_ShouldReturnPortfolioResponse_WhenValidRequest() {
        // Given
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        // When
        PortfolioResponse response = portfolioService.createPortfolio(createPortfolioRequest);

        // Then
        assertNotNull(response);
        assertEquals("Test Portfolio", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals("USD", response.getBaseCurrency());
        assertEquals(Portfolio.PortfolioStatus.ACTIVE, response.getStatus());

        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void getPortfolio_ShouldReturnPortfolioResponse_WhenPortfolioExists() {
        // Given
        Long portfolioId = 1L;
        when(portfolioRepository.findByIdWithPositions(portfolioId)).thenReturn(Optional.of(portfolio));

        // When
        PortfolioResponse response = portfolioService.getPortfolio(portfolioId);

        // Then
        assertNotNull(response);
        assertEquals(portfolioId, response.getId());
        assertEquals("Test Portfolio", response.getName());

        verify(portfolioRepository, times(1)).findByIdWithPositions(portfolioId);
    }

    @Test
    void getPortfolio_ShouldThrowException_WhenPortfolioNotFound() {
        // Given
        Long portfolioId = 999L;
        when(portfolioRepository.findByIdWithPositions(portfolioId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> portfolioService.getPortfolio(portfolioId));

        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).findByIdWithPositions(portfolioId);
    }

    @Test
    void deletePortfolio_ShouldDeletePortfolio_WhenPortfolioExists() {
        // Given
        Long portfolioId = 1L;
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        doNothing().when(portfolioRepository).deleteById(portfolioId);

        // When
        portfolioService.deletePortfolio(portfolioId);

        // Then
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(portfolioRepository, times(1)).deleteById(portfolioId);
    }

    @Test
    void deletePortfolio_ShouldThrowException_WhenPortfolioNotFound() {
        // Given
        Long portfolioId = 999L;
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> portfolioService.deletePortfolio(portfolioId));

        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(portfolioRepository, never()).deleteById(any());
    }
}
