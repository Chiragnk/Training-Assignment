package com.monetize360.invoicegeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private String name;
    private double price;
    private int quantity;
    private double amount;
    private double taxRate;
    private double totalAmount;
    public OrderDetailsDTO(String name, double price, int quantity, double taxRate) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.taxRate = taxRate;
    }


}
