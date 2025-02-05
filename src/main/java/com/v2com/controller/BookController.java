package com.v2com.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.bookDTO;
import com.v2com.entity.BookEntity;
import com.v2com.service.BookService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
            return Response.ok(bookService.createBook(bookEntity)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @Path("/{bookId}")
    public Response getBookById(@PathParam("bookId") UUID bookId) {
        try {
            return Response.ok(bookService.getBookById(bookId)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    public Response getBooksByFilter (@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(bookService.getBooksByFilters(filters)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}