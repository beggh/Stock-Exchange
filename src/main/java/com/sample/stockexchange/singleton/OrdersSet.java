package com.sample.stockexchange.singleton;

import java.util.HashMap;
import java.util.Map;

import com.sample.stockexchange.entity.BuyOrder;
import com.sample.stockexchange.entity.SellOrder;
import com.sample.stockexchange.entity.Stock;

public final class OrderSetStore implements IOrderSetStore {
    private final HashMap<Stock, BuyOrder> buyMap;
    private final HashMap<Stock, SellOrder> sellMap;

    private OrderSetStore() {
        buyMap = new HashMap<>();
        sellMap = new HashMap<>();
    }

    private static  OrderSetStore instance = null;

    public static OrderSetStore getInstance() {
        if(instance == null) {
            synchronized (OrderSetStore.class) {
                instance = new OrderSetStore();
            }
        }
        return instance;
    }

    @Override
    public Map<Stock, BuyOrder> getBuyOrderMap() {
        return getInstance().buyMap;
    }

    @Override
    public Map<Stock, SellOrder> getSellOrderMap() {
        return getInstance().sellMap;
    }
}