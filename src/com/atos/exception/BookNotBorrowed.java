package com.atos.exception;

public class BookNotBorrowed extends Exception {

    public BookNotBorrowed(int bookId) {
        super("Book with ID "+bookId+" is not borrowed");
    }

}