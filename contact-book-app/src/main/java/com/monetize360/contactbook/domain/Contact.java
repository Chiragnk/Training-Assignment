package com.monetize360.contactbook.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String dob;
}
