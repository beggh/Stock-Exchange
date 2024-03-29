package com.sample.stockexchange.entity;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * SellOrderSet is a set of {@link Order} of type SELL, sorted by price.
 */
public class SellOrderSet {
    private SortedSet<Order> orderSet;

    public SellOrderSet() {
        Comparator<Order> comparator = new SellOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Order> getOrderSet() {
        return orderSet;
    }
}

final class SellOrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        if (a.getId().equals(b.getId())) {
            return 0; // invalid orders
        }

        int timeCompare = a.getTime().compareTo(b.getTime());  // sorting based on time instead of price

        return timeCompare;
    }
}