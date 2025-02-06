package com.v2com.repository;

import java.util.UUID;

import com.v2com.entity.BookEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase<BookEntity, UUID> {

    private final EntityManager entityManager;

    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}