package com.srd.demo.repo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.srd.demo.entity.TradeMongo;


@Repository
public interface TradeMongoRepo extends MongoRepository<TradeMongo,String> {

}



