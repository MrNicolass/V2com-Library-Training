package com.v2com.repository;

import java.util.UUID;

import com.v2com.entity.LoanEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

@ApplicationScoped
public class LoanRepository implements PanacheRepositoryBase<LoanEntity, UUID> {

    private final EntityManager entityManager;

    public LoanRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UUID findLoanByBookId(UUID bookId) {
        try {
            var query = entityManager.createQuery(
                "SELECT l.id " +
                "FROM LoanEntity l " +
                "WHERE 1=1 " +
                "AND l.book.bookId = :bookId"
            , UUID.class).setParameter("bookId", bookId).getSingleResult();
    
            return (UUID) query;

        } catch(NoResultException notFound) {
            return bookId;
        } catch(NonUniqueResultException nonUnique) {
            throw new NonUniqueResultException("More than one loan found for this book, ask for support!"); 
        }
    }

    public UUID findLoanByUserAndBookId(UUID userId, UUID bookId) {
        try {
            var query = entityManager.createNativeQuery(
                "SELECT l.loanid " +
                "FROM loans l " +
                "WHERE 1=1 " +
                "AND l.userId = :userId " +
                "AND l.bookId = :bookId"
            , UUID.class).setParameter("userId", userId).setParameter("bookId", bookId).getSingleResult();
    
            return (UUID) query;

        } catch(NoResultException notFound) {
            return userId;
        } catch(NonUniqueResultException nonUnique) {
            throw new NonUniqueResultException("More than one loan found, ask for support!"); 
        }
    }
}