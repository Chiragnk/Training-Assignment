package com.monetize360.invoicegeneration.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private List<DessertDTO> desserts;
    private List<DrinkDTO>drinks;

}
