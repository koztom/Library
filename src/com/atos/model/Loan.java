package com.atos.model;

public class Loan {

    private Book borrowedBook;
    private Member borrower;

    public Loan (Book book, Member member){
        setBorrowedBook(book);
        setBorrower(member);
    }

    public Book getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(Book borrowedBook) {
        this.borrowedBook = borrowedBook;
    }

    public Member getBorrower() {
        return borrower;
    }

    public void setBorrower(Member borrower) {
        this.borrower = borrower;
    }
}
