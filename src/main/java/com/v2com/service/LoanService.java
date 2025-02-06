package com.v2com.service;

import com.v2com.entity.LoanEntity;
import com.v2com.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.LoanDTO;
import com.v2com.dto.bookDTO;
import com.v2com.entity.BookEntity;
import com.v2com.repository.LoanRepository;
import com.v2com.repository.UserRepository;
import com.v2com.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public LoanDTO createLoan(LoanDTO loanDTO) {
        try {
            if (loanDTO.getUserId() == null) {
                throw new IllegalArgumentException("You cannot loan a book without a user assigned!");
            } else if (loanDTO.getBookId() == null) {
                throw new IllegalArgumentException("What book do you want to loan? Select at least one!");
            } else if (loanDTO.getLoanDate() == null) {
                throw new IllegalArgumentException("When was the book loaned? Fill the date!");
            }

            //Both user and book must exists
            UserEntity userEntity = userRepository.findById(loanDTO.getUserId());
            BookEntity bookEntity = bookRepository.findById(loanDTO.getBookId());

            if (userEntity == null) {
                throw new IllegalArgumentException("User not found!");
            }
            if (bookEntity == null) {
                throw new IllegalArgumentException("Book not found!");
            }

            LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity, loanDTO.getLoanDate(), loanDTO.getLoanDueDate(), loanDTO.getReturnDate(), loanDTO.getLoanStatus());
            loanRepository.persist(loanEntity);
            
            loanDTO.setLoanId(loanEntity.getLoanId());
            return loanDTO;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public LoanDTO getLoanById(UUID loanId) {
        try {
            LoanEntity loanEntity = loanRepository.findById(loanId);

            if(loanEntity == null){
                throw new IllegalArgumentException("Loan not registered!");
            } else {
                return new LoanDTO(loanEntity.getLoanId(), loanEntity.getUser().getUserId(), loanEntity.getBook().getBookId(), loanEntity.getLoanDate(), loanEntity.getLoanDueDate(), loanEntity.getReturnDate(), loanEntity.getLoanStatus());
            }

        } catch (Exception e){
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    } 

    public List<LoanEntity> getAllLoans() {
        try {
            if(loanRepository.findAll().list().isEmpty()) {
                throw new IllegalArgumentException("No loans found!");
            } else {
                return loanRepository.findAll().list();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<LoanEntity> getLooksByFilters(Map<String, String> filters){
        List<LoanEntity> loans = this.getAllLoans();

        if (loans.isEmpty()) {
            throw new IllegalArgumentException("No loans found!");
        }

        for (String key : filters.keySet()) {
            if (!key.equals("user") && !key.equals("book") && !key.equals("loanDate" ) && !key.equals("loanDueDate") && !key.equals("returnDate") && !key.equals("loanStatus")) {
                throw new IllegalArgumentException("One or more filters are invalid!");
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
    }

    public LoanDTO deleteLoan(UUID loanId) {
        LoanDTO loan = this.getLoanById(loanId);

        if(loan != null) {
            try {
                UserEntity userEntity = userRepository.findById(loan.getUserId());
                BookEntity bookEntity = bookRepository.findById(loan.getBookId());

                LoanEntity loanEntity = new LoanEntity(userEntity, bookEntity, loan.getLoanDate(), loan.getLoanDueDate(), loan.getReturnDate(), loan.getLoanStatus());
                loanEntity.setLoanId(loanId);

                loanRepository.delete(loanEntity);

                return loan;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return loan;
        }
    }

    public LoanDTO updateLoan(UUID loanId, LoanDTO loanDTO) {
        LoanEntity loanEntity = loanRepository.findById(loanId);
        BookEntity bookEntity = bookRepository.findById(loanDTO.getBookId());

        if(loanEntity != null){
            try {
                loanEntity.setUser(loanEntity.getUser());
                loanEntity.setBook(loanEntity.getBook());
                loanEntity.setLoanDate(loanDTO.getLoanDate() != null ? loanDTO.getLoanDate() : loanEntity.getLoanDate());
                loanEntity.setLoanDueDate(loanDTO.getLoanDueDate() != null ? loanDTO.getLoanDueDate() : loanEntity.getLoanDueDate());
                loanEntity.setReturnDate(loanDTO.getReturnDate() != null ? loanDTO.getReturnDate() : loanEntity.getReturnDate());
                loanEntity.setLoanStatus(loanDTO.getLoanStatus() != null ? loanDTO.getLoanStatus() : loanEntity.getLoanStatus());
    
                loanRepository.persist(loanEntity);

                loanDTO.setLoanId(loanId);
                return loanDTO;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return loanDTO;
        }
    }
}