package com.v2com.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.bookDTO;
import com.v2com.entity.BookEntity;
import com.v2com.repository.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookEntity createBook(bookDTO bookDTO) {
        try {
            if (bookDTO.getTitle() == null) {
                throw new IllegalArgumentException("Title is required!");
            }
            else if (bookDTO.getAuthor() == null) {
                throw new IllegalArgumentException("Author is required!");
            } else if (bookDTO.getIsAvailable() == null) {
                throw new IllegalArgumentException("Availability is required!");
            }
            
            BookEntity bookEntity = new BookEntity(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn(), bookDTO.getPublicationDate(), bookDTO.getIsAvailable());

            bookRepository.persist(bookEntity);
            return bookEntity;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }
    
    public List<BookEntity> getAllBooks() {
        try {
            if(bookRepository.findAll().list().isEmpty()) {
                throw new IllegalArgumentException("No books found!");
            } else {
                return bookRepository.findAll().list();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public bookDTO getBookById(UUID bookId) {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            if(bookEntity != null) {
                return new bookDTO(bookEntity.getBookId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn(), bookEntity.getPublicationDate(), bookEntity.getIsAvailable());
            } else {
                throw new IllegalArgumentException("Book not found!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<BookEntity> getBooksByFilters(Map<String, String> filters) {
        List<BookEntity> books = this.getAllBooks();

        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found!");
        }

        for (String key : filters.keySet()) {
            if (!key.equals("title") && !key.equals("author") && !key.equals("isbn" ) && !key.equals("isAvailable") && !key.equals("publicationDate")) {
                throw new IllegalArgumentException("One or more filters are invalid!");
            }
        }

        books = filters.entrySet().stream().reduce(books, (filteredbooks, filter) -> filteredbooks.stream().filter(book -> {
                switch(filter.getKey()) {
                    case "title":
                        return book.getTitle().contains(filter.getValue());
                    case "author":
                        return book.getAuthor().contains(filter.getValue());
                    case "isbn":
                        return book.getIsbn().toString().toUpperCase().contains(filter.getValue());
                    case "publicationDate":
                        return book.getPublicationDate().toString().toUpperCase().contains(filter.getValue());
                    case "isAvailable":
                        return Boolean.toString(book.getIsAvailable()).equalsIgnoreCase(filter.getValue());
                    default:
                        return true;
                }
            //Collect the filtered books into a list and combine the results of the reduction
            }).collect(Collectors.toList()), (u1, u2) -> u1);

        return books;
    }

    public bookDTO deleteBook(UUID bookId) {
        bookDTO book = this.getBookById(bookId);

        if(book != null) {
            try {
                BookEntity bookEntity = new BookEntity(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationDate(), book.getIsAvailable());
                bookEntity.setBookId(book.getBookId());
                bookRepository.delete(bookEntity);
                return book;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return book;
        }
    }

    public bookDTO updateBook(UUID bookId, bookDTO bookDTO) {
        BookEntity bookEntity = bookRepository.findById(bookId);

        if(bookEntity != null){
            try {
                bookEntity.setTitle(bookDTO.getTitle() != null ? bookDTO.getTitle() : bookEntity.getTitle());
                bookEntity.setAuthor(bookDTO.getAuthor() != null ? bookDTO.getAuthor() : bookEntity.getAuthor());
                bookEntity.setIsbn(bookDTO.getIsbn() != null ? bookDTO.getIsbn() : bookEntity.getIsbn());
                bookEntity.setPublicationDate(bookDTO.getPublicationDate() != null ? bookDTO.getPublicationDate() : bookEntity.getPublicationDate());
                bookEntity.setIsAvailable(bookDTO.getIsAvailable() != null ? bookDTO.getIsAvailable() : bookEntity.getIsAvailable());
    
                bookRepository.persist(bookEntity);

                bookDTO.setBookId(bookId);
                return bookDTO;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return bookDTO;
        }
    }
}