package com.v2com.service;

import java.util.UUID;

import com.v2com.dto.ReservationDTO;
import com.v2com.entity.BookEntity;
import com.v2com.entity.LoanEntity;
import com.v2com.entity.ReservationEntity;
import com.v2com.entity.UserEntity;
import com.v2com.repository.BookRepository;
import com.v2com.repository.LoanRepository;
import com.v2com.repository.ReservationRepository;
import com.v2com.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReservationService(ReservationRepository reservationRepository, LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository){
        this.reservationRepository = reservationRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception{
        try {
            if (reservationDTO.getUserId() == null) {
                throw new IllegalArgumentException("You cannot reserve a book without a user assigned!");
            } else if (reservationDTO.getBookId() == null) {
                throw new IllegalArgumentException("What book do you want to reserve? Select at least one!");
            } else if (reservationDTO.getReservationDate() == null) {
                throw new IllegalArgumentException("When was the book reserved? Fill the date!");
            }

            //Both user and book must exists, loan is used only to be checked
            UserEntity userEntity = userRepository.findById(reservationDTO.getUserId());
            BookEntity bookEntity = bookRepository.findById(reservationDTO.getBookId());
            UUID loanUUID = loanRepository.findLoadByBookId(reservationDTO.getBookId());

            if(loanUUID == reservationDTO.getBookId()){
                LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity);
                loanRepository.persist(loanEntity);
                throw new Exception("There are no loans, so, we've registered one for you! Take your book whenever you want! Loan ID = " + loanEntity.getLoanId());
            }

            if (userEntity == null) {
                throw new IllegalArgumentException("User not found!");
            }
            if (bookEntity == null) {
                throw new IllegalArgumentException("Book not found!");
            }

            ReservationEntity reservationEntity = new ReservationEntity(userEntity, bookEntity, reservationDTO.getReservationDate(), reservationDTO.getStatus());
            reservationRepository.persist(reservationEntity);
            
            reservationDTO.setReservationId(reservationEntity.getReservationId());
            return reservationDTO;
        } catch (IllegalArgumentException il) {
            throw new IllegalArgumentException("Something went wrong...: " + il.getMessage());
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}