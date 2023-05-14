package com.example.bookflix.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.bookflix.entity.BooksByUser;

public interface BooksByUserRepository extends CassandraRepository<BooksByUser, String> {

    Slice<BooksByUser> findAllById(String id, Pageable pageable);
    
}
