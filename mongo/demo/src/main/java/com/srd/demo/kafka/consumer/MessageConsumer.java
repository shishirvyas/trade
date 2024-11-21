package com.srd.demo.kafka.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.srd.demo.entity.Trade;
import com.srd.demo.entity.TradeMongo;
import com.srd.demo.service.TradeMongoService;
import com.srd.demo.service.TradeSqlService;

@Component
public class MessageConsumer {

    private final TradeSqlService tradeSqlService;
    private final TradeMongoService tradeMongoService;

    @Autowired
    public MessageConsumer(TradeSqlService tradeSqlService, TradeMongoService tradeMongoService) {
        this.tradeSqlService = tradeSqlService;
        this.tradeMongoService = tradeMongoService;
    }

    @KafkaListener(topics = "my-topic", groupId = "my-group-id")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        
        // Convert the message to a Trade entity
        Trade trade = parseMessageToTrade(message);
        
        // Save the trade data to SQL
        tradeSqlService.saveTradeSql(trade);
        
        // Convert to TradeMongo entity for MongoDB
        TradeMongo tradeMongo = convertToTradeMongo(trade);
        
        // Save the trade data to MongoDB
        tradeMongoService.saveTrade(tradeMongo);
    }

    private Trade parseMessageToTrade(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Trade trade = null;
        try {
            // Convert JSON string to Trade object
            trade = objectMapper.readValue(message, Trade.class);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception or log error
        }
        return trade;
    }

    private TradeMongo convertToTradeMongo(Trade trade) {
        // Convert Trade to TradeMongo
        TradeMongo tradeMongo = new TradeMongo();
        tradeMongo.setTradeId(trade.getTradeId());
        tradeMongo.setVersion(trade.getVersion());
        tradeMongo.setCounterPartyId(trade.getCounterPartyId());
        tradeMongo.setMaturityDate(trade.getMaturityDate());
        tradeMongo.setCreatedDate(trade.getCreatedDate());
        tradeMongo.setExpired(trade.isExpired());
        tradeMongo.setId(trade.getId()+"");
        return tradeMongo;
    }
}
