package com.v2com.repository;

import java.util.UUID;

import com.v2com.entity.LoanEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, UUID> {

    private final EntityManager entityManager;

    public LoanRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UUID findLoadByBookId(UUID bookId){
        var query = entityManager.createQuery(
            "SELECT l.id " +
            "FROM LoanEntity l " +
            "WHERE 1=1 " +
            "AND l.book.id = :bookId", UUID.class).setParameter("bookId", bookId).getResultList();

        if(query.isEmpty()){
            return bookId;
        } else {
            return query.get(0);
        }
    }
}