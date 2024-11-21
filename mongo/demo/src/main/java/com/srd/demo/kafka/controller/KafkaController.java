package com.srd.demo.kafka.controller;

import com.srd.demo.kafka.producer.MessageProducer;
import com.srd.demo.entity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private MessageProducer messageProducer;

   /*  @PostMapping("/send")
    public String sendMessage(@RequestParam("msg") String message, @RequestParam("msg1") String message1) {
        messageProducer.sendMessage("my-topic", message);
        messageProducer.sendMessage("my-topic", message1);
        return "Message sent: " + message + " " + message1;
    }*/

    @PostMapping("/sendTrade")
    public String sendTrade(@RequestBody Trade trade) {
        // Convert the Trade object to JSON or another suitable format
        String tradeMessage = convertTradeToMessage(trade);
        messageProducer.sendMessage("my-topic", tradeMessage);
        return "Trade sent: " + trade.getTradeId();
    }

    private String convertTradeToMessage(Trade trade) {
        // Convert the Trade object to a string format (e.g., JSON or CSV) for Kafka
        return String.format("{\"tradeId\":\"%s\", \"version\":%d, \"counterPartyId\":\"%s\", \"maturityDate\":\"%s\", \"createdDate\":\"%s\", \"expired\":%b}",
                trade.getTradeId(),
                trade.getVersion(),
                trade.getCounterPartyId(),
                trade.getMaturityDate(),
                trade.getCreatedDate(),
                trade.isExpired());
    }
}
