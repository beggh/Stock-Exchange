package com.sample.stockexchange.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.google.common.base.Splitter;
import com.sample.stockexchange.entity.Order;
import com.sample.stockexchange.entity.OrderEntry;
import com.sample.stockexchange.entity.OrderType;
import com.sample.stockexchange.entity.Stock;
import com.sample.stockexchange.exception.customException;
import com.sample.stockexchange.repository.OrderRepository;

public class orderController {
    private final OrderRepository repo;

    public orderController(OrderRepository repo) {
        this.repo = repo;
    }

    public Order parse(String input) {
        Splitter spaceSplitter = Splitter.on(' ').omitEmptyStrings().trimResults();
        Iterator<String> tokenItr = spaceSplitter.split(input).iterator();

        String orderId = tokenItr.next();

        String timeStr = tokenItr.next();
        LocalTime orderTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()));

        String stockName = tokenItr.next();
        Stock stock = new Stock(stockName);

        String typeStr = tokenItr.next();
        OrderType type = OrderType.valueOf(typeStr.toUpperCase());
        BigDecimal price = new BigDecimal(tokenItr.next());
        int quantity = Integer.parseInt(tokenItr.next());

        return new Order(orderId, orderTime, stock ,type, price, quantity);
    }

    public List<Order> readFromCLI(Scanner input_orders) {
        List<Order> orders = new ArrayList<>();
        try {
            while (input_orders.hasNextLine()) {
                orders.add(parse(input_orders.nextLine()));
            }
        } catch (DateTimeParseException | NoSuchElementException | NumberFormatException e) {
            System.out.println("Invalid input format! Exception: " + e.getMessage());
        }

        return orders;
    }

    public void writeToCLI(List<OrderEntry> entries) {
        entries.forEach((entry) -> {
            String output = String.format("%s %.2f %d %s", entry.getParty().getId(), entry.getExecutionPrice(),
                    entry.getQuantity(), entry.getCounterParty().getId());
            System.out.println(output);
        });
    }

    public void run(File file) {
        repo.cleanup();

        try {
            Scanner sc = new Scanner(file);
            repo.addOrders(readFromCLI(sc));

            writeToCLI(repo.processOrders());
        } catch (customException e) {
            System.out.println("Invalid input orders! Exception: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}