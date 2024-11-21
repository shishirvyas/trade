package com.srd.demo.repo.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.srd.demo.entity.Trade;

@Repository
public interface TradeSqlRepo extends JpaRepository<Trade, Integer> {
    Trade findByTradeId(String tradeId);
     List<Trade> findAllByExpiredFalseAndMaturityDateBefore(LocalDate date);
}


