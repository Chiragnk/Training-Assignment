package com.monetize360.spring_data_jpa_tutorial.repository;

import com.monetize360.spring_data_jpa_tutorial.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Integer> {
}
