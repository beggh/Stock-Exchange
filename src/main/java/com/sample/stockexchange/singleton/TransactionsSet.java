package com.sample.stockexchange.singleton;

import java.util.ArrayList;
import java.util.List;

import com.sample.stockexchange.entity.TradeEntry;

public final class TransactionStore implements ITransactionStore {
    private final List<TradeEntry> orderEntries;

    private TransactionStore() {
        orderEntries = new ArrayList<>();
    }


    private static  TransactionStore instance = new TransactionStore();


    public static TransactionStore getInstance() {
        if(instance ==null) {
            synchronized (TransactionStore.class) {
                instance = new TransactionStore();
            }
        }
        return instance;
    }

    @Override
    public List<TradeEntry> getOrderEntries() {
        return getInstance().orderEntries;
    }
}