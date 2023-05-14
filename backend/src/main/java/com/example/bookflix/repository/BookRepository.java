package com.example.bookflix.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.example.bookflix.entity.Book;

@Repository
public interface BookRepository extends CassandraRepository<Book, String> {
    
}