package com.srd.demo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trades")  // MongoDB-specific annotation
public class TradeMongo implements Serializable {

    @Id
    private String id;  // MongoDB generates a unique ID for documents.

    private String tradeId;
    private Integer version;
    private String counterPartyId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private boolean expired;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    // Default constructor
    public TradeMongo() {}

    // Parameterized constructor
    public TradeMongo(String tradeId, Integer version, String counterPartyId, LocalDate maturityDate, LocalDate createdDate, boolean expired) {
        this.tradeId = tradeId;
        this.version = version;
        this.counterPartyId = counterPartyId;
        this.maturityDate = maturityDate;
        this.createdDate = createdDate;
        this.expired = expired;
    }
}
