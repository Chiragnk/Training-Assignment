package com.monetize360.cbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean deleted;

}
