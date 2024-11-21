package com.srd.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.srd.demo.entity.TradeMongo;
import com.srd.demo.repo.mongo.TradeMongoRepo;
import com.srd.demo.exceptions.VersionMismatchException;

@Service
@Transactional
public class TradeMongoService {
    @Autowired
    private TradeMongoRepo tradeMongoRepo;

    @Autowired
    public TradeMongoService(TradeMongoRepo tMongoRepo) {
        this.tradeMongoRepo = tMongoRepo;
    }

    public void saveTrade(TradeMongo tradeMongo) {
        // Fetch existing trade from MongoDB
        TradeMongo existingTrade = tradeMongoRepo.findById(tradeMongo.getId()).orElse(null);
    
        if (existingTrade != null) {
            if (existingTrade.getVersion() > tradeMongo.getVersion()) {
                // Reject lower version
                throw new VersionMismatchException("Received trade has a lower version than the current record.");
            } 
            // If versions are the same or higher, replace the current trade
            tradeMongoRepo.save(tradeMongo);
        } else {
            // Save new trade if no existing trade found
            tradeMongoRepo.save(tradeMongo);
        }
    }
    
}
