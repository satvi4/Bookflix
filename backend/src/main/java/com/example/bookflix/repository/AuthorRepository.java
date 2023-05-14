package com.example.bookflix.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.example.bookflix.entity.Author;

@Repository
public interface AuthorRepository extends CassandraRepository<Author, String> {
    
}