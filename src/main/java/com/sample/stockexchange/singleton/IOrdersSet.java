package com.sample.stockexchange.singleton;

import java.util.Map;

import com.sample.stockexchange.entity.BuyOrder;
import com.sample.stockexchange.entity.SellOrder;
import com.sample.stockexchange.entity.Stock;

/**
 * Interface for persisting incoming orders based stock. For simplicity's sake,
 * just a map for this implementation
 */
public interface IOrderSetStore {
    public Map<Stock, BuyOrder> getBuyOrderMap();

    public Map<Stock, SellOrder> getSellOrderMap();
}