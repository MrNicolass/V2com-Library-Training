package com.v2com.repository;

import java.util.UUID;

import com.v2com.entity.ReservationEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ReservationRepository implements PanacheRepositoryBase<ReservationEntity, UUID>{

    private final EntityManager entityManager;

    public ReservationRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    
}
