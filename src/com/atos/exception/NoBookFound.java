package com.atos.exception;

public class NoBookFound extends Exception {

    public NoBookFound() {
        super("No book was found");
    }

}