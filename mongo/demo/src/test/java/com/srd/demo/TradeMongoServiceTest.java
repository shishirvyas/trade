package com.srd.demo;

import com.srd.demo.entity.TradeMongo;
import com.srd.demo.exceptions.VersionMismatchException;
import com.srd.demo.repo.mongo.TradeMongoRepo;
import com.srd.demo.service.TradeMongoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeMongoServiceTest {

    @Mock
    private TradeMongoRepo tradeMongoRepo;

    @InjectMocks
    private TradeMongoService tradeMongoService;

    private TradeMongo existingTrade;
    private TradeMongo newTrade;

    @BeforeEach
    public void setup() {
        existingTrade = new TradeMongo("T123", 2, "C123", null, null, false);
        newTrade = new TradeMongo("T123", 2, "C123", null, null, false);
    }

    @Test
    public void testSaveNewTrade() {
        // Arrange: No existing trade in the database
        when(tradeMongoRepo.findById(newTrade.getId())).thenReturn(Optional.empty());

        // Act
        tradeMongoService.saveTrade(newTrade);

        // Assert: New trade should be saved
        verify(tradeMongoRepo, times(1)).save(newTrade);
    }

    @Test
    public void testSaveTradeWithLowerVersionThrowsException() {
        // Arrange: An existing trade with a higher version
        newTrade.setVersion(1); // lower version than existing
        when(tradeMongoRepo.findById(newTrade.getId())).thenReturn(Optional.of(existingTrade));

        // Act & Assert
        VersionMismatchException thrown = assertThrows(VersionMismatchException.class, () -> {
            tradeMongoService.saveTrade(newTrade);
        });
        assertEquals("Received trade has a lower version than the current record.", thrown.getMessage());

        // Verify that save is never called because of the exception
        verify(tradeMongoRepo, never()).save(newTrade);
    }

    @Test
    public void testSaveTradeWithSameVersionReplacesExistingTrade() {
        // Arrange: An existing trade with the same version
        when(tradeMongoRepo.findById(newTrade.getId())).thenReturn(Optional.of(existingTrade));

        // Act
        tradeMongoService.saveTrade(newTrade);

        // Assert: Existing trade should be replaced
        verify(tradeMongoRepo, times(1)).save(newTrade);
    }

    @Test
public void testSaveTradeWithHigherVersionReplacesExistingTrade() {
    // Arrange: Set up existing trade and new trade
    existingTrade.setId("T123");
    existingTrade.setVersion(2);  // Lower version
    newTrade.setId("T123");
    newTrade.setVersion(3);  // Higher version

    // Mock findById to return the existing trade
    when(tradeMongoRepo.findById("T123")).thenReturn(Optional.of(existingTrade));

    // Act
    tradeMongoService.saveTrade(newTrade);

    // Assert: Verify that save was called with the new trade
    verify(tradeMongoRepo, times(1)).save(newTrade);
}

}

