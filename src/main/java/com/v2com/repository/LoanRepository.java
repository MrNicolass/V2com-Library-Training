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

    public UUID findLoadByBookId(UUID bookId) {
        var query = entityManager.createQuery(
            "SELECT l.id " +
            "FROM LoanEntity l " +
            "WHERE 1=1 " +
            "AND l.book.bookId = :bookId"
        , UUID.class).setParameter("bookId", bookId).getResultList();

        if (query.isEmpty()) {
            return bookId;
        } else {
            return (UUID) query.get(0);
        }
    }

    public UUID findLoanByUserIdAndBook(UUID userId, UUID bookId) {
        var query = entityManager.createNativeQuery(
            "SELECT l.loanid " +
            "FROM loans l " +
            "WHERE 1=1 " +
            "AND l.userId = :userId " +
            "AND l.bookId = :bookId"
        , UUID.class).setParameter("userId", userId).setParameter("bookId", bookId).getResultList();

        if (query.isEmpty()) {
            return userId;
        } else {
            return (UUID) query.get(0);
        }
    }
}