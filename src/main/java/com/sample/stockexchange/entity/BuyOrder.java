package com.sample.stockexchange.entity;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class BuyOrderSet {
    private SortedSet<Order> orderSet;

    public BuyOrderSet() {
        Comparator<Order> comparator = new BuyOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Order> getOrderSet() {
        return orderSet;
    }
}

final class BuyOrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        if (a.getId().equals(b.getId())) {
            return 0; // invalid orders
        }

       int priceCompare = b.getAskingPrice().compareTo(a.getAskingPrice()); // higher price gets higher priority, descending sort
        if(priceCompare == 0){
            return a.getTime().compareTo(b.getTime());
        }
        return priceCompare;
    }
}