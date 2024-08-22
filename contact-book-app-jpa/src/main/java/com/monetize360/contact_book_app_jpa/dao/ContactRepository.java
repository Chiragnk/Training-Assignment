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

    /*@Modifying
    @Query("UPDATE Contact c SET c.firstName = :firstName, c.lastName = :lastName, c.email = :email, c.phone = :phone, c.deleted = :deleted WHERE c.id = :id")
    void updateContact(@Param("id") UUID id,
                       @Param("firstName") String firstName,
                       @Param("lastName") String lastName,
                       @Param("email") String email,
                       @Param("phone") String phone,
                       @Param("deleted") boolean deleted);

    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    Optional<Contact> findContactById(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Contact c SET c.deleted = true WHERE c.id = :id")
    void softDeleteContact(@Param("id") UUID id);

    @Query("SELECT c FROM Contact c")
    Page<Contact> findAllContacts(Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.firstName LIKE %:search% OR c.lastName LIKE %:search% OR c.email LIKE %:search% OR c.phone LIKE %:search%")
    Page<Contact> searchContacts(@Param("search") String search, Pageable pageable);*/
    }


