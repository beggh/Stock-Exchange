package com.sample.stockexchange.service;

import static com.sample.stockexchange.entity.OrderType.BUY;
import static com.sample.stockexchange.entity.OrderType.SELL;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sample.stockexchange.exception.customException;
import com.sample.stockexchange.singleton.IOrderSetStore;
import com.sample.stockexchange.singleton.ITransactionStore;
import com.sample.stockexchange.entity.BuyOrderSet;
import com.sample.stockexchange.entity.Order;
import com.sample.stockexchange.entity.OrderEntry;
import com.sample.stockexchange.entity.SellOrderSet;
import com.sample.stockexchange.entity.Stock;

public final class OrderUsecasesRepo {

    private final Map<Stock, BuyOrderSet> buys;
    private final Map<Stock, SellOrderSet> sells;
    private final List<OrderEntry> transactionList;

    public OrderUsecasesRepo(IOrderSetStore orderStore, ITransactionStore transactionStore) {
        this.buys = orderStore.getBuyOrderStore();
        this.sells = orderStore.getSellOrderStore();
        this.transactionList = transactionStore.getOrderEntries();
    }

    public void addOrders(List<Order> orders) throws customException, IOException {
        if (orders == null || orders.isEmpty()) {
            return;
        }

        for (Order order : orders) {
            if (order == null) {
                continue;
            }

            if (order.getStock() == null) {
                throw new IOException("No stocks attached to Order: " + order.getId());
            }

            Set<Order> orderSet = null;
            if (order.getType() == BUY) {
                BuyOrderSet buyOrders = buys.get(order.getStock());
                if (buyOrders == null) {
                    buyOrders = new BuyOrderSet();
                    buys.put(order.getStock(), buyOrders);
                }
                orderSet = buyOrders.getOrderSet();
            } else if (order.getType() == SELL) {
                SellOrderSet sellOrders = sells.get(order.getStock());
                if (sellOrders == null) {
                    sellOrders = new SellOrderSet();
                    sells.put(order.getStock(), sellOrders);
                }
                orderSet = sellOrders.getOrderSet();
            }

            if (orderSet.contains(order)) {
                throw new customException("Order is possibly duplicated: " + order.getId());
            } else {
                orderSet.add(order);
            }
        }
    }

    public void cleanup() {
        buys.clear();
        sells.clear();
        transactionList.clear();
    }


    public List<OrderEntry> processOrders() throws IOException {
        if (buys == null || buys.isEmpty() || sells == null || sells.isEmpty()) {
            //return transactionList;
            if(!transactionList.isEmpty()){
                return transactionList;
            }
            else throw new IOException("Invalid Input provided");

        }

        // process buy orders
        buys.forEach((stock, orders) -> {
            Set<Order> buyOrderSet = orders.getOrderSet();

            if (buyOrderSet == null || buyOrderSet.isEmpty()) {
                return;
            }

            SellOrderSet sOrderSet = sells.get(stock);
            if (sOrderSet == null) {
                return;
            }

            Set<Order> sellOrderSet = sOrderSet.getOrderSet();

            buyOrderSet.stream().filter(order -> (order.getQuantity() > 0)).forEach((buy) -> {
                for (Order sell : sellOrderSet) {
                    if (sell.getQuantity() > 0 && buy.getAskingPrice().compareTo(sell.getAskingPrice()) >= 0) {   // execution logic

                        int qty = 0;
                        if (sell.getQuantity() > buy.getQuantity()) {
                            qty = buy.getQuantity();
                            sell.setQuantity(sell.getQuantity() - buy.getQuantity());
                            buy.setQuantity(0);
                        } else {
                            qty = sell.getQuantity();
                            buy.setQuantity(buy.getQuantity() - sell.getQuantity());
                            sell.setQuantity(0);
                        }

                        // record it in order entry
                        OrderEntry entry = new OrderEntry(buy, sell.getAskingPrice(), qty, sell);
                        transactionList.add(entry);
                    }
                }
            });
        });

        return transactionList;
    }
}