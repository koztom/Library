package com.atos.tests;

import com.atos.dao.implementation.BookDAO;
import com.atos.exception.BookNotFoundException;
import com.atos.exception.NoBookFound;
import com.atos.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class BookDAOTest {
    private BookDAO dao;
    private static final Book BOOK = new Book("Homo deus", "Yuval Noah Harari",2017);

    @BeforeEach
    public void setUp() {
        dao = new BookDAO();
        dao.save(BOOK);

        Map<Integer,Book> books = dao.getAll();
        assertEquals(BOOK, books.get(BOOK.getId()));
    }

    @Nested
    public class NonExistingBook {

        @Test
        public void addingShouldBeSuccess() throws Exception {
            try (Stream<Map.Entry<Integer, Book>> allBooks = dao.getAll().entrySet().stream()) {
                assumeTrue(allBooks.count() == 1);
            }

            final Book nonExistingBook = new Book( "Zamek", "Franz Kafka",1983);
            dao.save(nonExistingBook);
            assertTrue(dao.getAll().containsKey(nonExistingBook.getId()));
            assertBookCountIs(2);
            assertEquals(nonExistingBook, dao.get(nonExistingBook.getId()));
        }

        @Test
        public void deletionShouldThrowException(){
            final Book nonExistingBook = new Book( "Zamek", "Franz Kafka",1983);

            Assertions.assertThrows(BookNotFoundException.class, () -> dao.delete(nonExistingBook.getId()));
            assertFalse(dao.getAll().containsKey(nonExistingBook.getId()));
            assertBookCountIs(1);
        }


        @Test
        public void getShouldThrowBookNotFoundException(){
            Assertions.assertThrows(BookNotFoundException.class, () -> dao.get(getNonExistingBookId()));

        }
    }

    @Nested
    public class ExistingBook {

        @Test
        public void savingSecondTimeShouldntAddToBooks() throws Exception {
            dao.save(BOOK);
            assertBookCountIs(1);
            assertEquals(BOOK, dao.get(BOOK.getId()));
        }

        @Test
        public void deletionShouldBeSuccess() throws Exception {
            dao.delete(BOOK.getId());
            assertBookCountIs(0);
            assertFalse(dao.getAll().containsKey(BOOK.getId()));
        }

        @Test
        public void getShouldReturnTheBook() throws BookNotFoundException {
            Book retrievedBook = dao.get(BOOK.getId());
            assertEquals(BOOK, retrievedBook);
        }
    }

    @Nested
    public class FewBooksToFind {
        private final Book firstBookToAdd = new Book( "Zamek", "Franz Kafka",1983);
        private final Book secondBookToAdd = new Book( "Przemiana", "Franz Kafka",1912);

        @BeforeEach
        public void setUp() {
            dao.save(BOOK);
            dao.save(firstBookToAdd);
            dao.save(secondBookToAdd);
            Map<Integer,Book> books = dao.getAll();
            assertEquals(BOOK, books.get(BOOK.getId()));
            assertEquals(firstBookToAdd, books.get(firstBookToAdd.getId()));
            assertEquals(secondBookToAdd, books.get(secondBookToAdd.getId()));
        }

        @Test
        public void shouldThrowExceptionAsThereAreNoParameters()  {
            Assertions.assertThrows(NoBookFound.class, () -> dao.findBookByParameters("","",""));
        }

        @Test
        public void shouldThrowExceptionAsThereIsNonNumericYearPassed()  {
            Assertions.assertThrows(NumberFormatException.class, () -> dao.findBookByParameters("","","Franz Kafka"));
        }

        @Test
        public void shouldReturnBooksFromGivenYear()  {
            List<Book> found = new ArrayList<>();
            try {
                found = dao.findBookByParameters("","","1983");
            } catch (NumberFormatException | NoBookFound bookException) {
                bookException.printStackTrace();
            }
            assertTrue(found.contains(firstBookToAdd));
            assertEquals(found.size(),1);
        }

        @Test
        public void shouldReturnBooksFromGivenAuthor()  {
            List<Book> found = new ArrayList<>();
            try {
                found = dao.findBookByParameters("Franz Kafka","","");
            } catch (NumberFormatException | NoBookFound bookException) {
                bookException.printStackTrace();
            }
            assertTrue(found.contains(firstBookToAdd));
            assertTrue(found.contains(secondBookToAdd));
            assertEquals(found.size(),2);
        }

        @Test
        public void shouldNotReturnAnyBook()  {
            Assertions.assertThrows(NoBookFound.class, () -> dao.findBookByParameters("Franz Kafka","Proces",""));
        }
    }

    private int getNonExistingBookId() {
        return 999;
    }

    private void assertBookCountIs(int count) {
        try (Stream<Map.Entry<Integer, Book>> allBooks = dao.getAll().entrySet().stream()) {
            assertEquals(count, allBooks.count());
        }
    }
}