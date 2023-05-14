package com.example.bookflix.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.example.bookflix.entity.UserBooks;
import com.example.bookflix.entity.UserBooksPrimaryKey;

public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
    
}