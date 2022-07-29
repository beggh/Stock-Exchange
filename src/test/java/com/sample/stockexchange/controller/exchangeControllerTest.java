package com.sample.stockexchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import com.sample.stockexchange.singleton.OrderSetStore;
import com.sample.stockexchange.singleton.TransactionStore;
import com.sample.stockexchange.entity.Order;
import com.sample.stockexchange.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class orderControllerTest {
    private orderController controller;

    @BeforeEach
    void instantiateController() {
        OrderRepository repo = new OrderRepository(OrderSetStore.getInstance(), TransactionStore.getInstance());
        this.controller = new orderController(repo);
    }

    @Test
    void testValidInput() {
        Order order = controller.parse("   #9   10:02 BAC buy 242.70 150 ");

        assertEquals("#9", order.getId());
        assertEquals("10:02", order.getTime().toString());
        assertEquals("BAC", order.getStock().getName());
        assertEquals("buy", order.getType().name().toLowerCase());
        assertEquals(150, order.getQuantity());
        assertEquals(new BigDecimal("242.70"), order.getAskingPrice());
    }

    @Test
    void testInvalidInputFormat() {
        assertThrows(NoSuchElementException.class, () -> {
            controller.parse("#1 09:01 XXX buy");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.parse("#2 09:02 XXX 848");
        });

        assertThrows(DateTimeParseException.class, () -> {
            controller.parse("#3 XXX 848");
        });

        assertThrows(NumberFormatException.class, () -> {
            controller.parse("#4 12:12 XXX buy 848 efghj");
        });
    }

    @Test
    void testEmptyInput() {
        assertThrows(NoSuchElementException.class, () -> {
            controller.parse("");
        });
    }
}