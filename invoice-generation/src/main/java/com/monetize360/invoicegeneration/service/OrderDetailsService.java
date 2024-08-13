package com.monetize360.invoicegeneration.service;

import com.monetize360.invoicegeneration.dto.OrderDetailsDTO;

public class OrderDetailsService {
    public void calculateAmounts(OrderDetailsDTO orderDetails) {
        double amount = orderDetails.getPrice() * orderDetails.getQuantity();
        double totalAmount = amount + (amount * orderDetails.getTaxRate() / 100);
        orderDetails.setAmount(amount);
        orderDetails.setTotalAmount(totalAmount);
    }
}

