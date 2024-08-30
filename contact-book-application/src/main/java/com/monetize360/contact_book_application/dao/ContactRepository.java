package com.monetize360.contact_book_application.dao;

import com.monetize360.contact_book_application.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Page<Contact> findByFirstNameContainingOrLastNameContainingOrEmailContainingOrPhoneContaining(String search, String search1, String search2, String search3, PageRequest pageRequest);
}
