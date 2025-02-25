package com.v2com.service;

import com.v2com.entity.LoanEntity;
import com.v2com.entity.UserEntity;
import com.v2com.entity.enums.LoanStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.Exceptions.BookNotFoundException;
import com.v2com.Exceptions.FilterInvalidException;
import com.v2com.Exceptions.LoanDateIsNullException;
import com.v2com.Exceptions.LoanNotFoundException;
import com.v2com.Exceptions.OtherUserLoanedException;
import com.v2com.Exceptions.UserAlreadyLoanedException;
import com.v2com.Exceptions.UserNotFoundException;
import com.v2com.dto.LoanDTO;
import com.v2com.dto.ReservationDTO;
import com.v2com.entity.BookEntity;
import com.v2com.repository.LoanRepository;
import com.v2com.repository.UserRepository;
import com.v2com.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReservationService reservationService;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository, ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
    }

    public LoanDTO createLoan(LoanDTO loanDTO) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(loanDTO.getUserId());
            BookEntity bookEntity = bookRepository.findById(loanDTO.getBookId());
            UUID locateLoan = loanRepository.findLoanByBookId(loanDTO.getBookId());
            UUID locateUserLoan = loanRepository.findLoanByUserAndBookId(loanDTO.getUserId(), loanDTO.getBookId());

            if (loanDTO.getUserId() == null) {
                throw new UserNotFoundException("You cannot loan a book without a user assigned!");
            } else if (loanDTO.getBookId() == null) {
                throw new BookNotFoundException("What book do you want to loan? Select at least one!");
            } else if (loanDTO.getLoanDate() == null) {
                throw new LoanDateIsNullException();
            } else if (userEntity == null) {
                throw new UserNotFoundException();
            } else if (bookEntity == null) {
                throw new BookNotFoundException();
            } else if (!loanDTO.getUserId().equals(locateUserLoan)) {
                throw new UserAlreadyLoanedException(locateUserLoan.toString());
            } else if (!loanDTO.getBookId().equals(locateLoan)) {
                ReservationDTO reservDTO = new ReservationDTO();
                reservDTO.setUserId(loanDTO.getUserId());
                reservDTO.setBookId(loanDTO.getBookId());

                ReservationDTO createDTO = reservationService.createReservation(reservDTO);
                throw new OtherUserLoanedException(createDTO.getReservationId());
            }

            LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity, loanDTO.getLoanDate(), loanDTO.getLoanDueDate(), loanDTO.getReturnDate(), loanDTO.getLoanStatus());
            loanRepository.persist(loanEntity);
            
            loanDTO.setLoanId(loanEntity.getLoanId());
            return loanDTO;
        } catch (UserNotFoundException user) {
            throw user;
        } catch (UserAlreadyLoanedException loaned) {
            throw loaned;
        } catch (BookNotFoundException book) {
            throw book;
        } catch (LoanDateIsNullException loanDate) {
            throw loanDate;
        } catch (OtherUserLoanedException reservation) {
            throw reservation;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public LoanDTO getLoanById(UUID loanId) throws Exception  {
        try {
            LoanEntity loanEntity = loanRepository.findById(loanId);

            if (loanEntity == null) {
                throw new LoanNotFoundException(loanId.toString());
            } else {
                return new LoanDTO(loanEntity.getLoanId(), loanEntity.getUser().getUserId(), loanEntity.getBook().getBookId(), loanEntity.getLoanDate(), loanEntity.getLoanDueDate(), loanEntity.getReturnDate(), loanEntity.getLoanStatus());
            }

        } catch (LoanNotFoundException notFound){
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    } 

    public List<LoanEntity> getAllLoans() throws Exception {
        try {
            if (loanRepository.findAll().list().isEmpty()) {
                throw new LoanNotFoundException();
            } else {
                return loanRepository.findAll().list();
            }
        } catch (LoanNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<LoanEntity> getLoansByFilters(Map<String, String> filters) throws Exception{
        try {
            List<LoanEntity> loans = this.getAllLoans();
    
            if (loans.isEmpty()) {
                throw new LoanNotFoundException();
            }
    
            for (String key : filters.keySet()) {
                if (!key.equals("user") && !key.equals("book") && !key.equals("loanDate" ) && !key.equals("loanDueDate") && !key.equals("returnDate") && !key.equals("loanStatus")) {
                    throw new FilterInvalidException(key);
                }
            }
    
            loans = filters.entrySet().stream().reduce(loans, (filteredloans, filter) -> filteredloans.stream().filter(loan -> {
                    switch(filter.getKey()) {
                        case "user":
                            return loan.getUser().getUserId().toString().contains(filter.getValue());
                        case "book":
                            return loan.getBook().getBookId().toString().contains(filter.getValue());
                        case "loanDate":
                            return loan.getLoanDate().toString().contains(filter.getValue());
                        case "loanDueDate":
                            return loan.getLoanDueDate().toString().contains(filter.getValue());
                        case "returnDate":
                            return loan.getReturnDate().toString().contains(filter.getValue());
                        case "loanStatus":
                            return loan.getLoanStatus().toString().toUpperCase().contains(filter.getValue());
                        default:
                            return true;
                    }
                //Collect the filtered loans into a list and combine the results of the reduction
                }).collect(Collectors.toList()), (u1, u2) -> u1);
    
            return loans;

        } catch (LoanNotFoundException notFound) {
            throw notFound;
        } catch (FilterInvalidException invalid) {
            throw invalid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public LoanDTO deleteLoan(UUID loanId) throws Exception {
        try {
            LoanDTO loan = this.getLoanById(loanId);

            if(loan != null) {
                UserEntity userEntity = userRepository.findById(loan.getUserId());
                BookEntity bookEntity = bookRepository.findById(loan.getBookId());

                if (userEntity == null) {
                    throw new UserNotFoundException();
                } else if (bookEntity == null) {
                    throw new BookNotFoundException();
                }

                LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity);
                loanEntity.setLoanId(loanId);

                loanRepository.delete(loanEntity);

                return loan;
            } else {
                throw new LoanNotFoundException(loanId.toString());
            }
            
        } catch (LoanNotFoundException notFound) {
            throw notFound;
        } catch (UserNotFoundException user) {
            throw user;
        } catch (BookNotFoundException book) {
            throw book;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }
    
    public LoanDTO updateLoan(UUID loanId, LoanDTO loanDTO) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findById(loanId);
            BookEntity bookEntityChange = bookRepository.findById(loanDTO.getBookId());
            UserEntity userEntityChange = userRepository.findById(loanDTO.getUserId());
            
            if (userEntityChange == null) {
                throw new UserNotFoundException();
            } else if (bookEntityChange == null) {
                throw new BookNotFoundException();
            } else if (loanEntity != null) {
                loanEntity.setUser(userEntityChange != null ? userEntityChange : loanEntity.getUser());
                loanEntity.setBook(bookEntityChange != null ? bookEntityChange : loanEntity.getBook());
                loanEntity.setLoanDate(loanDTO.getLoanDate() != null ? loanDTO.getLoanDate() : loanEntity.getLoanDate());
                loanEntity.setLoanDueDate(loanDTO.getLoanDueDate() != null ? loanDTO.getLoanDueDate() : loanEntity.getLoanDueDate());
                loanEntity.setReturnDate(loanDTO.getReturnDate() != null ? loanDTO.getReturnDate() : loanEntity.getReturnDate());

                if (loanEntity.getReturnDate().equals(new java.sql.Date(System.currentTimeMillis())) || loanEntity.getReturnDate().before(loanEntity.getLoanDueDate())) {
                    loanEntity.setLoanStatus(LoanStatus.RETURNED);
                } else if (loanEntity.getReturnDate().after(loanEntity.getLoanDueDate()) || (loanEntity.getReturnDate() == null && loanEntity.getLoanDueDate().after(new java.sql.Date(System.currentTimeMillis())))) {
                    loanEntity.setLoanStatus(LoanStatus.LATE);
                } else {
                    loanEntity.setLoanStatus(loanDTO.getLoanStatus() != null ? loanDTO.getLoanStatus() : loanEntity.getLoanStatus());
                }

                loanRepository.persist(loanEntity);

                loanDTO.setLoanId(loanId);
                return loanDTO;
            } else {
                throw new LoanNotFoundException(loanId.toString());
            }

        } catch (UserNotFoundException user) {
            throw user;
        } catch (BookNotFoundException book) {
            throw book;
        } catch (LoanNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }
}