package com.monetize360.contact_book_app_jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="contact_jpa")
public class Contact {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;
        @Column(name = "firstname")
        private String firstName;
        @Column(name = "lastname")
        private String lastName;
        private String email;
        private String phone;
        private boolean deleted;
}
