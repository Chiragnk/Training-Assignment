package com.monetize360.invoicegeneration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Drink {
    private String name;
    private double price;
    private int quantity;
}
