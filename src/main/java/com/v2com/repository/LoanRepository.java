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
}