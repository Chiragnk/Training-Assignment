package com.monetize360.iplstats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TeamAmountByRoleDTO {
    private String label;
    private String role;
    private int amount;
}
