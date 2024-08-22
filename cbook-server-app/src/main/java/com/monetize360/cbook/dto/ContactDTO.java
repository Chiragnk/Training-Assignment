package com.monetize360.cbook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ContactDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean deleted;
}
