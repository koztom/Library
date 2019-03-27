package com.atos.tests;

import com.atos.Library;
import com.atos.dao.implementation.BookDAO;
import com.atos.dao.implementation.LoanDAO;
import com.atos.dao.implementation.MemberDAO;
import com.atos.exception.BookCurrentlyBorrowed;
import com.atos.exception.BookNotBorrowed;
import com.atos.exception.BookNotFoundException;
import com.atos.exception.MemberNotFoundException;
import com.atos.model.Book;
import com.atos.model.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private Library library;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private LoanDAO loanDAO;
    private static Book BOOK;
    private static final Member MEMBER = new Member("John Traven");


    @BeforeEach
    public void setUp() {
        BOOK = new Book("Homo deus", "Yuval Noah Harari",2017);
        library = Library.getInstance();
        bookDAO =library.getBookDao();
        memberDAO = library.getMemberDao();
        loanDAO = library.getLoanDAO();
        bookDAO.save(BOOK);
        memberDAO.save(MEMBER);
    }

    @AfterEach
    public void resetSingleton() throws Exception {
        Field instance = Library.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void deletionShouldBeSuccess() throws BookNotFoundException, BookCurrentlyBorrowed {
        library.removeBook(BOOK.getId());
        assertBookCountIs(0);
        assertFalse(bookDAO.getAll().containsKey(BOOK.getId()));
    }

    @Test
    public void deletionShouldThrowException() {
        BOOK.setAvailable(false);
        Assertions.assertThrows(BookCurrentlyBorrowed.class, () -> library.removeBook(BOOK.getId()));
        assertBookCountIs(1);
        assertTrue(bookDAO.getAll().containsKey(BOOK.getId()));
    }

    @Test
    public void shouldCreateNewMember() throws MemberNotFoundException {
        String name = "David Jones";
        library.findMember(name);
        assertEquals(2,memberDAO.getAll().size());
        memberDAO.getByName(name);

        assertTrue(memberDAO.getAll().values().stream().anyMatch(item -> name.equals(item.getName())));
    }

    @Test
    public void shouldCreateNewLoan() throws BookNotFoundException, BookCurrentlyBorrowed, BookNotBorrowed {
        library.borrowBook(BOOK.getId(),MEMBER.getName());
        assertEquals(1,loanDAO.getAll().size());
        assertFalse(BOOK.getAvailable());
        assertEquals(loanDAO.get(BOOK.getId()).getBorrowedBook().getId(),BOOK.getId());

        assertEquals(loanDAO.get(BOOK.getId()).getBorrower().getId(),MEMBER.getId());

    }

    @Test
    public void borrowingSameBookAgainShouldFailure() throws BookNotFoundException, BookCurrentlyBorrowed, BookNotBorrowed {
        library.borrowBook(BOOK.getId(),MEMBER.getName());
        Assertions.assertThrows(BookCurrentlyBorrowed.class, () -> library.borrowBook(BOOK.getId(),"David Jones"));
        assertEquals(1,loanDAO.getAll().size());
        assertEquals(loanDAO.get(BOOK.getId()).getBorrower().getId(),MEMBER.getId());
        assertEquals(2,memberDAO.getAll().size());
    }

    @Test
    public void borrowingNonExistingBookShouldFailure() {

        Assertions.assertThrows(BookNotFoundException.class, () -> library.borrowBook(getNonExistingBookId(),MEMBER.getName()));
        assertEquals(0,loanDAO.getAll().size());
        assertFalse(memberDAO.getAll().containsKey(getNonExistingBookId()));
    }

    @Test
    public void returningShouldBeSuccessful() throws BookNotFoundException, BookCurrentlyBorrowed, BookNotBorrowed {
        library.borrowBook(BOOK.getId(),MEMBER.getName());
        assertFalse(BOOK.getAvailable());
        assertEquals(1,loanDAO.getAll().size());

        library.returnBook(BOOK.getId());
        assertEquals(0,loanDAO.getAll().size());
        assertTrue(BOOK.getAvailable());
    }

    @Test
    public void returningShouldThrowBookNotFoundException() throws BookNotFoundException, BookCurrentlyBorrowed {
        library.borrowBook(BOOK.getId(),MEMBER.getName());
        Assertions.assertThrows(BookNotFoundException.class, () -> library.returnBook(getNonExistingBookId()));
        assertFalse(BOOK.getAvailable());
        assertEquals(1,loanDAO.getAll().size());
    }

    @Test
    public void returningShouldFailure() {
        Assertions.assertThrows(BookNotBorrowed.class, () -> library.returnBook(BOOK.getId()));
        assertTrue(BOOK.getAvailable());
        assertEquals(0,loanDAO.getAll().size());
    }

    private int getNonExistingBookId() {
        return 999;
    }

    private void assertBookCountIs(int count) {
        try (Stream<Map.Entry<Integer, Book>> allBooks = bookDAO.getAll().entrySet().stream()) {
            assertEquals(count, allBooks.count());
        }
    }
}
