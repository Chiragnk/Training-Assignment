package com.monetize360.employeedetails.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    private String currency;
    private int decimals;
    private String symbol;
}
