package com.srd.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.srd.demo.entity.Trade;
import com.srd.demo.repo.jpa.TradeSqlRepo;
import com.srd.demo.exceptions.VersionMismatchException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TradeSqlService 
{
    @Autowired
    private TradeSqlRepo tradeSqlRepo;

    @Autowired
    public TradeSqlService(TradeSqlRepo tradeSqlRepo) {
        this.tradeSqlRepo = tradeSqlRepo;
    }

    public void saveTradeSql(Trade trade) {
        if (trade.getTradeId() == null) {
            throw new NullPointerException("Trade ID cannot be null");
        }

        // Reject trades with a maturity date earlier than today
        if (trade.getMaturityDate() != null && trade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Trade has a maturity date earlier than today's date.");
        }
        
        // Fetch existing trade from SQL
        Trade existingTrade = tradeSqlRepo.findByTradeId(trade.getTradeId());
    
        if (existingTrade != null) {
            if (existingTrade.getVersion() > trade.getVersion()) {
                // Reject lower version
                throw new VersionMismatchException("Received trade has a lower version than the current record.");
            }
            // If versions are the same or higher, replace the current trade
            if (existingTrade.getVersion().equals(trade.getVersion()) || existingTrade.getVersion() < trade.getVersion()) {
                tradeSqlRepo.save(trade);  // This should be called when the versions are the same or higher
            }
        } else {
            // Save new trade if no existing trade found
            tradeSqlRepo.save(trade);  // This will be called when there is no existing trade
        }
    }
       // Scheduled method to mark trades as expired if the maturity date has passed
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void markExpiredTrades() {
    List<Trade> tradesToExpire = tradeSqlRepo.findAllByExpiredFalseAndMaturityDateBefore(LocalDate.now());

    if (tradesToExpire != null) {
        tradesToExpire.stream()
            .filter(Objects::nonNull)
            .forEach(trade -> {
                trade.setExpired(true);
                tradeSqlRepo.save(trade);
            });
    }
}

}
