package com.sample.stockexchange;

import com.sample.stockexchange.singleton.OrderSetStore;
import com.sample.stockexchange.singleton.TransactionStore;
import com.sample.stockexchange.controller.orderController;
import com.sample.stockexchange.service.OrderUsecasesRepo;

public class StockExchangeApp {
    public static void main(String[] args) {
        // initialize usecase repo
        OrderUsecasesRepo repo = new OrderUsecasesRepo(OrderSetStore.getInstance(), TransactionStore.getInstance());

        // initialize controller
        orderController controller = new orderController(repo);

        // execute
        controller.run();
    }
}
