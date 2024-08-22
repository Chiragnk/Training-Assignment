package com.monetize360.contact_book_app_jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean deleted;
}
