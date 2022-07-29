package com.sample.stockexchange.singleton;

import java.util.List;

import com.sample.stockexchange.entity.TradeEntry;

/**
 * Interface for persisting order book containing executed orders. For
 * simplicity's sake, just a list for this implementation
 * 
 */
public interface ITransactionStore {
    public List<TradeEntry> getOrderEntries();
}