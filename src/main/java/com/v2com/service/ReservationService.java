package com.v2com.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
            UserEntity userEntity = userRepository.findById(reservationDTO.getUserId());
            BookEntity bookEntity = bookRepository.findById(reservationDTO.getBookId());
            UUID loanUUID = loanRepository.findLoadByBookId(reservationDTO.getBookId());
            
            if (reservationDTO.getUserId() == null) {
                throw new IllegalArgumentException("You cannot reserve a book without a user assigned!");
            } else if (reservationDTO.getBookId() == null) {
                throw new IllegalArgumentException("What book do you want to reserve? Select at least one!");
            } else if (reservationDTO.getReservationDate() == null) {
                throw new IllegalArgumentException("When was the book reserved? Fill the date!");
            } else if (userEntity == null) {
                throw new IllegalArgumentException("User not found!");
            } else if (bookEntity == null) {
                throw new IllegalArgumentException("Book not found!");
            } else if(loanUUID == reservationDTO.getBookId()){
                LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity);
                loanRepository.persist(loanEntity);
                throw new Exception("There are no loans, so, we've registered one for you! Take your book whenever you want! Loan ID: " + loanEntity.getLoanId());
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

    public List<ReservationEntity> getAllReservations() {
        try{
            if(reservationRepository.findAll().list().isEmpty()) {
                throw new IllegalArgumentException("No loans found!");
            } else {
                return reservationRepository.findAll().list();
            } 
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public ReservationDTO getReservationById(UUID reservationId) {
        try{
            ReservationEntity reservationEntity = reservationRepository.findById(reservationId);

            if(reservationEntity == null){
                throw new IllegalArgumentException("Reservation not found!");
            } else {
                return new ReservationDTO(reservationEntity.getReservationId(), reservationEntity.getUser().getUserId(), reservationEntity.getBook().getBookId(), reservationEntity.getReservationDate(), reservationEntity.getStatus());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<ReservationEntity> getReservationsByFilters(Map<String, String> filters){
        List<ReservationEntity> reservations = this.getAllReservations();

        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("No reservations found!");
        }

        for (String key : filters.keySet()) {
            if (!key.equals("user") && !key.equals("book") && !key.equals("reservationDate" ) && !key.equals("status")) {
                throw new IllegalArgumentException("One or more filters are invalid!");
            }
        }

        reservations = filters.entrySet().stream().reduce(reservations, (filteredreservations, filter) -> filteredreservations.stream().filter(reservation -> {
                switch(filter.getKey()) {
                    case "user":
                        return reservation.getUser().getUserId().toString().contains(filter.getValue());
                    case "book":
                        return reservation.getBook().getBookId().toString().contains(filter.getValue());
                    case "reservationDate":
                        return reservation.getReservationDate().toString().contains(filter.getValue());
                    case "status":
                        return reservation.getStatus().toString().contains(filter.getValue());
                    default:
                        return true;
                }
            //Collect the filtered reservations into a list and combine the results of the reduction
            }).collect(Collectors.toList()), (u1, u2) -> u1);

        return reservations;
    }

    public ReservationDTO deleteReservation(UUID reservationId) {
        ReservationDTO reservation = this.getReservationById(reservationId);

        if(reservation != null) {
            try {
                UserEntity userEntity = userRepository.findById(reservation.getUserId());
                BookEntity bookEntity = bookRepository.findById(reservation.getBookId());

                ReservationEntity reservationEntity = new ReservationEntity(userEntity, bookEntity, reservation.getReservationDate(), reservation.getStatus());
                reservationEntity.setReservationId(reservationId);

                reservationRepository.delete(reservationEntity);

                return reservation;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return reservation;
        }
    }

    public ReservationDTO updateReservation(UUID reservationId, ReservationDTO reservationDTO) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId);
        
        BookEntity bookEntityChange = bookRepository.findById(reservationDTO.getBookId());
        if(bookEntityChange == null){
            throw new IllegalArgumentException("Book does not exists!");
        }

        UserEntity userEntityChange = userRepository.findById(reservationDTO.getUserId());
        if(userEntityChange == null){
            throw new IllegalArgumentException("User does not exists!");
        }

        if(reservationEntity != null){
            try {
                reservationEntity.setUser(userEntityChange != null ? userEntityChange : reservationEntity.getUser());
                reservationEntity.setBook(bookEntityChange != null ? bookEntityChange : reservationEntity.getBook());
                reservationEntity.setReservationDate(reservationDTO.getReservationDate() != null ? reservationDTO.getReservationDate() : reservationEntity.getReservationDate());
                reservationEntity.setStatus(reservationDTO.getStatus() != null ? reservationDTO.getStatus() : reservationEntity.getStatus());
    
                reservationRepository.persist(reservationEntity);

                reservationDTO.setReservationId(reservationId);
                return reservationDTO;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return reservationDTO;
        }
    }
}