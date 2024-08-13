package com.monetize360.invoicegeneration.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {
    private String name;
    private double price;
    private int quantity;
    private double amount;
    private double taxRate;
    private double totalAmount;
}
