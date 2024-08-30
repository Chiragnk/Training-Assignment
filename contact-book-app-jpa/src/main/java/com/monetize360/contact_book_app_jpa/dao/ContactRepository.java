package com.monetize360.contact_book_app_jpa.dao;
import org.apache.xmlbeans.impl.tool.Extension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;
import com.monetize360.contact_book_app_jpa.domain.Contact;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Page<Contact> findByFirstNameContainingOrLastNameContainingOrEmailContainingOrPhoneContaining(String search, String search1, String search2, String search3, PageRequest pageRequest);
    }


