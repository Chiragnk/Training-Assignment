package com.monetize360.contactbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String dob;

}
