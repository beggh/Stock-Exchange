package com.sample.stockexchange.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderEntry {
    private final UUID id;
    private final Order party;
    private final Order counterParty;
    private final int quantity;
    private final BigDecimal executionPrice;

    public OrderEntry(Order party,  BigDecimal executionPrice, int quantity, Order counterParty) {
        this.id = UUID.randomUUID();
        this.party = party;
        this.executionPrice = executionPrice;
        this.quantity = quantity;
        this.counterParty = counterParty;
    }

    public UUID getId() {
        return id;
    }

    public Order getParty() {
        return this.party;
    }

    public Order getCounterParty() {
        return this.counterParty;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getExecutionPrice() {
        return this.executionPrice;
    }
}