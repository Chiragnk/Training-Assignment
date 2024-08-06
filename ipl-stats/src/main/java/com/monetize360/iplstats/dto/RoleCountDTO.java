package com.monetize360.iplstats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RoleCountDTO {
    private String roleName;
    private int count;

}
