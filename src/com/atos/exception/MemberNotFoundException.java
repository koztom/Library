package com.atos.exception;

public class MemberNotFoundException extends Exception {

    public MemberNotFoundException(String name) {
        super("No member with name "+name+" was found");
    }

}
