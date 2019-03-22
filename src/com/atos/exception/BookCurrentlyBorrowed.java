package com.atos.exception;

public class BookCurrentlyBorrowed extends Exception {

    public BookCurrentlyBorrowed(int bookToBorrowId) {
        super("Book with ID "+bookToBorrowId+" is currently borrowed");
    }

}