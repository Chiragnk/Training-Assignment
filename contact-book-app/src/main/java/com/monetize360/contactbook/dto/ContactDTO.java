package com.monetize360.contactbook.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class ContactDTO {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String dob;

}
