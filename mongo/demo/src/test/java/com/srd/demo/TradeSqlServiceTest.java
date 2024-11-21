package com.srd.demo;

import com.srd.demo.entity.Trade;
import com.srd.demo.exceptions.VersionMismatchException;
import com.srd.demo.repo.jpa.TradeSqlRepo;
import com.srd.demo.service.TradeSqlService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Using Mockito with JUnit 5
public class TradeSqlServiceTest {

    @Mock
    private TradeSqlRepo tradeSqlRepo;

    @InjectMocks
    private TradeSqlService tradeSqlService;

    private Trade existingTrade;
    private Trade newTrade;
    private Trade expiredTrade;

    @BeforeEach
    public void setUp() {
        // Mock the existing trade
        existingTrade = new Trade();
        existingTrade.setTradeId("T12345");
        existingTrade.setVersion(2);
        existingTrade.setCounterPartyId("CP001");
        existingTrade.setMaturityDate(LocalDate.of(2024, 12, 31));
        existingTrade.setCreatedDate(LocalDate.of(2024, 11, 9));
        existingTrade.setExpired(false);

        // Mock the new trade to be saved
        newTrade = new Trade();
        newTrade.setTradeId("T12345");
        newTrade.setVersion(3);
        newTrade.setCounterPartyId("CP001");
        newTrade.setMaturityDate(LocalDate.of(2024, 12, 31));
        newTrade.setCreatedDate(LocalDate.of(2024, 11, 9));
        newTrade.setExpired(false);

        expiredTrade = new Trade();
        expiredTrade.setTradeId("T7890");
        expiredTrade.setVersion(1);
        expiredTrade.setCounterPartyId("CP002");
        expiredTrade.setMaturityDate(LocalDate.now().minusDays(1)); // Maturity date in the past
        expiredTrade.setExpired(false);
    }

    @Test
    public void testSaveNewTrade() {
        // Arrange: No trade exists in DB (findByTradeId will return null)
        when(tradeSqlRepo.findByTradeId(newTrade.getTradeId())).thenReturn(null);

        // Act: Save the new trade
        tradeSqlService.saveTradeSql(newTrade);

        // Assert: Verify the save method was called on the repository
        verify(tradeSqlRepo, times(1)).save(newTrade);
    }

    @Test
    public void testSaveTradeWithSameVersion() {
        // Arrange: Trade with the same version already exists
        when(tradeSqlRepo.findByTradeId(existingTrade.getTradeId())).thenReturn(existingTrade);

        // Act: Try to save the trade with the same version
        tradeSqlService.saveTradeSql(existingTrade);

        // Assert: Verify that the save method was called
        verify(tradeSqlRepo, times(1)).save(existingTrade);
    }

    @Test
    public void testSaveTradeWithHigherVersion() {
        // Arrange: Trade with lower version already exists
        when(tradeSqlRepo.findByTradeId(existingTrade.getTradeId())).thenReturn(existingTrade);

        // Act: Try to save the trade with a higher version
        tradeSqlService.saveTradeSql(newTrade);

        // Assert: Verify that the save method was called on the repository
        verify(tradeSqlRepo, times(1)).save(newTrade);
    }

    @Test
    public void testSaveTradeWithLowerVersionThrowsException() {
        // Arrange: Create existing trade with a higher version
        Trade existingTrade = new Trade();
        existingTrade.setTradeId("T12345");
        existingTrade.setVersion(2);  // Higher version in DB

        // Create new trade with a lower version
        Trade newTrade = new Trade();
        newTrade.setTradeId("T12345");
        newTrade.setVersion(1);  // Lower version being saved

        // Mock the behavior of findByTradeId() to return the existing trade
        when(tradeSqlRepo.findByTradeId("T12345")).thenReturn(existingTrade);

        // Act & Assert: Expect VersionMismatchException when saving a lower version trade
        VersionMismatchException thrown = assertThrows(VersionMismatchException.class, () -> {
            tradeSqlService.saveTradeSql(newTrade);
        });

        // Assert: Verify the exception message
        assertEquals("Received trade has a lower version than the current record.", thrown.getMessage());
    }

    @Test
    public void testSaveTradeWithNoTradeId() {
        // Arrange: Trade has no tradeId
        Trade tradeWithoutId = new Trade();
        tradeWithoutId.setTradeId(null);

        // Act & Assert: Attempting to save a trade without tradeId should throw an exception or handle it
        assertThrows(NullPointerException.class, () -> tradeSqlService.saveTradeSql(tradeWithoutId));
    }

    @Test
    public void testRejectTradeWithExpiredMaturityDate() {
        // Arrange: Set up trade with a maturity date earlier than today
        Trade expiredTrade = new Trade();
        expiredTrade.setTradeId("T54321");
        expiredTrade.setVersion(1);
        expiredTrade.setMaturityDate(LocalDate.now().minusDays(1));  // Maturity date in the past

        // Act & Assert: Expect IllegalArgumentException when saving a trade with an expired maturity date
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tradeSqlService.saveTradeSql(expiredTrade);
        });

        // Assert: Verify the exception message
        assertEquals("Trade has a maturity date earlier than today's date.", thrown.getMessage());
    }

    @Test
    public void testMarkExpiredTrades() {
        // Arrange: Stub to return a list with expiredTrade
        when(tradeSqlRepo.findAllByExpiredFalseAndMaturityDateBefore(LocalDate.now()))
            .thenReturn(Arrays.asList(expiredTrade));

        // Act: Call the method to mark trades as expired
        tradeSqlService.markExpiredTrades();

        // Assert: Verify the trade was marked as expired and saved
        assertTrue(expiredTrade.isExpired(), "The trade should be marked as expired.");
        verify(tradeSqlRepo, times(1)).save(expiredTrade);
    }
    
}
