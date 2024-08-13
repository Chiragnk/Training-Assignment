package com.monetize360.invoicegeneration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private List<Dessert> desserts;
    private List<Drink>drinks;

}
