package com.v2com.repository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

@ApplicationScoped
public class AuthRepository {
    
    private final EntityManager entityManager;

    public AuthRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String[] findEmail(String email) {
        try {
            Object[] query = (Object[]) entityManager.createNativeQuery(
                "SELECT email, role " +
                "FROM users " +
                "WHERE 1=1 " +
                "AND email = :email"
                ).setParameter("email", email).getSingleResult();

            String[] results = new String[2];
            for (int i = 0; i < 2; i++) {
                results[i] = query[i].toString();
            }

            return results;
                
        } catch (NoResultException notFound) {
            String[] results = new String[2];
            return results;
        } catch (NonUniqueResultException nonUnique) {
            throw new NonUniqueResultException("More than one e-mail founded, please, ask for support!"); 
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public boolean validatePassword(String email, String password) {
        try {

            var storedPassword = entityManager.createNativeQuery(
                "SELECT password " +
                "FROM users " +
                "WHERE 1=1 " +
                "AND email = :email"
                , String.class).setParameter("email", email).getSingleResult().toString();

            boolean passwordEquals = BCrypt.verifyer().verify(password.toCharArray(), storedPassword).verified;

            return passwordEquals;

            
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

}