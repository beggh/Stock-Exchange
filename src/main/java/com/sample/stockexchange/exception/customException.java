package com.sample.stockexchange.exception;

/**
 * AddOrderException may be thrown while trying to add an order to
 * {@link com.sample.stockexchange.entity.BuyOrderSet}
 */
public class Exception extends RuntimeException {
    private static final long serialVersionUID = 6111010203853573098L;

    public Exception(String msg) {
        super(msg);
    }
}