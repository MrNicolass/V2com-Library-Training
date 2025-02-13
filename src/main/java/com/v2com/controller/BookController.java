package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.Exceptions.ArgumentNullException;
import com.v2com.Exceptions.BookNotFoundException;
import com.v2com.Exceptions.FilterInvalidException;
import com.v2com.dto.bookDTO;
import com.v2com.service.BookService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/v1/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookController {
    
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @POST
    @Transactional
    public Response createBook(bookDTO bookEntity) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book created!");
            response.put("book", bookService.createBook(bookEntity));
            return Response.status(201).entity(response).build();

        } catch (ArgumentNullException argumentRequired) {
            return Response.status(406).entity(argumentRequired.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @Path("/{bookId}")
    public Response getBookById(@PathParam("bookId") UUID bookId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book found!");
            response.put("book", bookService.getBookById(bookId));
            return Response.ok(response).build();

        } catch (BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    public Response getBooksByFilter(@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(bookService.getBooksByFilters(filters)).build();

        } catch (BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (FilterInvalidException invalid) {
            return Response.status(406).entity(invalid.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{bookId}")
    public Response deleteBook(@PathParam("bookId") UUID bookId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book deleted!");
            response.put("book", bookService.deleteBook(bookId));
            return Response.status(410).entity(response).build();

        } catch (BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @Path("/{bookId}")
    public Response updateBook(@PathParam("bookId") UUID bookId, bookDTO bookDTO) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book updated!");
            response.put("book", bookService.updateBook(bookId, bookDTO));
            return Response.ok(response).build();

        } catch (BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}