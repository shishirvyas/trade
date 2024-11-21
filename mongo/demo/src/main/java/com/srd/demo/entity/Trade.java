package com.srd.demo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trades")
public class Trade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name="id")
    private int id;

    @Column(name = "trade_id", nullable = false)
    private String tradeId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "counter_party_id", nullable = false)
    private String counterPartyId;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    // Default constructor
    public Trade() {}

    // Parameterized constructor
    public Trade(int id, String tradeId, Integer version, String counterPartyId) {
       // this.id = id;
        this.tradeId = tradeId;
        this.version = version;
        this.counterPartyId = counterPartyId;
        this.createdDate = LocalDate.now();
        this.maturityDate = this.createdDate.plusDays(3);
        this.expired = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
