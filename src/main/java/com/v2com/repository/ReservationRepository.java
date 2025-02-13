package com.v2com.repository;

import java.util.UUID;

import com.v2com.entity.ReservationEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

@ApplicationScoped
public class ReservationRepository implements PanacheRepositoryBase<ReservationEntity, UUID>{

    private final EntityManager entityManager;

    public ReservationRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public UUID findReservationByUserAndBookId(UUID userId, UUID bookId) {
        try {
            var query = entityManager.createNativeQuery(
                "SELECT r.reservationId " +
                "FROM reservations r " +
                "WHERE 1=1 " +
                "AND r.userId = :userId " +
                "AND r.bookId = :bookId"
                , UUID.class).setParameter("userId", userId).setParameter("bookId", bookId).getSingleResult();

                return (UUID) query;
                
        } catch(NoResultException notFound) {
            return userId;
        } catch(NonUniqueResultException nonUnique) {
            throw new NonUniqueResultException("More than one reservation found, ask for support!"); 
        }
    }
    
}
