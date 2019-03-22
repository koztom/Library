package com.atos.exception;

public class BookNotFoundException extends Exception {

    public BookNotFoundException(int bookId) {
        super("No book with ID "+Integer.toString(bookId));
    }

}