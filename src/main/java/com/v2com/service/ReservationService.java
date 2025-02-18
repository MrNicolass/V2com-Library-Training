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
import com.v2com.exceptions.BookNotFoundException;
import com.v2com.exceptions.FilterInvalidException;
import com.v2com.exceptions.LoanDateIsNullException;
import com.v2com.exceptions.LoanNotFoundException;
import com.v2com.exceptions.NoLoansFoundException;
import com.v2com.exceptions.OtherUserLoanedException;
import com.v2com.exceptions.ReservationDateIsNullException;
import com.v2com.exceptions.ReservationNotFoundException;
import com.v2com.exceptions.UserAlreadyLoanedException;
import com.v2com.exceptions.UserAlreadyReservedException;
import com.v2com.exceptions.UserNotFoundException;
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

    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(reservationDTO.getUserId());
            BookEntity bookEntity = bookRepository.findById(reservationDTO.getBookId());
            UUID loanUUID = loanRepository.findLoanByBookId(reservationDTO.getBookId());
            UUID locateUserReservation = reservationRepository.findReservationByUserAndBookId(reservationDTO.getUserId(), reservationDTO.getBookId());
            
            if (reservationDTO.getUserId() == null) {
                throw new UserNotFoundException("You cannot reserve a book without a user assigned!");
            } else if (reservationDTO.getBookId() == null) {
                throw new BookNotFoundException("What book do you want to reserve? Select at least one!");
            } else if (reservationDTO.getReservationDate() == null) {
                throw new ReservationDateIsNullException();
            } else if (userEntity == null) {
                throw new UserNotFoundException(reservationDTO.getUserId());
            } else if (bookEntity == null) {
                throw new BookNotFoundException(reservationDTO.getBookId());
            } else if (loanUUID == reservationDTO.getBookId()) {
                LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity);
                loanRepository.persist(loanEntity);
                throw new NoLoansFoundException(loanEntity.getLoanId());
            } else if (locateUserReservation.equals(reservationDTO.getUserId())) {
                throw new UserAlreadyReservedException();
            }

            ReservationEntity reservationEntity = new ReservationEntity(userEntity, bookEntity, reservationDTO.getReservationDate(), reservationDTO.getStatus());
            reservationRepository.persist(reservationEntity);
            
            reservationDTO.setReservationId(reservationEntity.getReservationId());
            return reservationDTO;

        } catch (UserNotFoundException | BookNotFoundException notFound) {
            throw notFound;
        } catch (ReservationDateIsNullException reservationDate) {
            throw reservationDate;
        } catch (NoLoansFoundException LoanNotFound) {
            throw LoanNotFound;
        } catch (UserAlreadyReservedException alreadyReserv) {
            throw alreadyReserv;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<ReservationEntity> getAllReservations() throws Exception {
        try{
            if(reservationRepository.findAll().list().isEmpty()) {
                throw new ReservationNotFoundException();
            } else {
                return reservationRepository.findAll().list();
            } 
            
        } catch(ReservationNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public ReservationDTO getReservationById(UUID reservationId) throws Exception {
        try{
            ReservationEntity reservationEntity = reservationRepository.findById(reservationId);

            if(reservationEntity == null){
                throw new ReservationNotFoundException();
            } else {
                return new ReservationDTO(reservationEntity.getReservationId(), reservationEntity.getUser().getUserId(), reservationEntity.getBook().getBookId(), reservationEntity.getReservationDate(), reservationEntity.getStatus());
            }
        } catch (ReservationNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<ReservationEntity> getReservationsByFilters(Map<String, String> filters) throws Exception {
        try {
            List<ReservationEntity> reservations = this.getAllReservations();
    
            if (reservations.isEmpty()) {
                throw new ReservationNotFoundException();
            }
    
            for (String key : filters.keySet()) {
                if (!key.equals("user") && !key.equals("book") && !key.equals("reservationDate" ) && !key.equals("status")) {
                    throw new FilterInvalidException(key);
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

        } catch (ReservationNotFoundException notFound) {
            throw notFound;
        } catch (FilterInvalidException invalid) {
            throw invalid;
        } catch(Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public ReservationDTO deleteReservation(UUID reservationId) throws Exception {
        try {
            ReservationDTO reservation = this.getReservationById(reservationId);

            if(reservation != null) {
                UserEntity userEntity = userRepository.findById(reservation.getUserId());
                BookEntity bookEntity = bookRepository.findById(reservation.getBookId());

                if(userEntity == null) {
                    throw new UserNotFoundException();
                } else if(bookEntity == null) {
                    throw new BookNotFoundException();
                }

                ReservationEntity reservationEntity = new ReservationEntity(userEntity, bookEntity, reservation.getReservationDate(), reservation.getStatus());
                reservationEntity.setReservationId(reservationId);

                reservationRepository.delete(reservationEntity);

                return reservation;
            } else {
                throw new ReservationNotFoundException();
            }

        } catch (ReservationNotFoundException notFound) {
            throw notFound;
        } catch (UserNotFoundException user) {
            throw user;
        } catch (BookNotFoundException book) {
            throw book;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public ReservationDTO updateReservation(UUID reservationId, ReservationDTO reservationDTO) throws Exception {
        try {
            ReservationEntity reservationEntity = reservationRepository.findById(reservationId);
            BookEntity bookEntityChange = bookRepository.findById(reservationDTO.getBookId());
            UserEntity userEntityChange = userRepository.findById(reservationDTO.getUserId());
            
            if(userEntityChange == null) {
                throw new UserNotFoundException();
            } else if(bookEntityChange == null) {
                throw new BookNotFoundException();
            } else if(reservationEntity != null) {
                reservationEntity.setUser(userEntityChange != null ? userEntityChange : reservationEntity.getUser());
                reservationEntity.setBook(bookEntityChange != null ? bookEntityChange : reservationEntity.getBook());
                reservationEntity.setReservationDate(reservationDTO.getReservationDate() != null ? reservationDTO.getReservationDate() : reservationEntity.getReservationDate());
                reservationEntity.setStatus(reservationDTO.getStatus() != null ? reservationDTO.getStatus() : reservationEntity.getStatus());

                reservationRepository.persist(reservationEntity);

                reservationDTO.setReservationId(reservationId);
                return reservationDTO;
            } else {
                throw new ReservationNotFoundException(reservationId.toString());
            }

        } catch (UserNotFoundException user) {
            throw user;
        } catch (BookNotFoundException book) {
            throw book;
        } catch (ReservationNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

}