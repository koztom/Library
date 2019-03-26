package com.atos.tests;

import com.atos.dao.implementation.LoanDAO;
import com.atos.exception.BookNotBorrowed;
import com.atos.model.Book;
import com.atos.model.Loan;
import com.atos.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class LoanDAOTest {
    private LoanDAO dao;
    private static final Book BOOK = new Book("Homo deus", "Yuval Noah Harari", 2017);
    private static final Member MEMBER = new Member("John Traven");
    private static final Loan LOAN = new Loan(BOOK, MEMBER);

    @BeforeEach
    public void setUp() {
        dao = new LoanDAO();
        dao.save(LOAN);

        Map<Integer, Loan> loans = dao.getAll();
        assertEquals(LOAN, loans.get(LOAN.getBorrowedBook().getId()));
    }

    @Nested
    public class NonExistingLoan {

        @Test
        public void addingShouldBeSuccess() throws BookNotBorrowed {
            try (Stream<Map.Entry<Integer, Loan>> allLoans = dao.getAll().entrySet().stream()) {
                assumeTrue(allLoans.count() == 1);
            }

            final Loan nonExistingLoan = new Loan(new Book("Zamek", "Franz Kafka", 1983), new Member("David Jones"));
            dao.save(nonExistingLoan);
            assertTrue(dao.getAll().containsKey(nonExistingLoan.getBorrowedBook().getId()));
            assertLoanCountIs(2);
            assertEquals(nonExistingLoan, dao.get(nonExistingLoan.getBorrowedBook().getId()));
        }

        @Test
        public void deletionShouldThrowException() {
            final Loan nonExistingLoan = new Loan(new Book("Zamek", "Franz Kafka", 1983), new Member("David Jones"));

            Assertions.assertThrows(BookNotBorrowed.class, () -> dao.delete(nonExistingLoan.getBorrowedBook().getId()));
            assertFalse(dao.getAll().containsKey(nonExistingLoan.getBorrowedBook().getId()));
            assertLoanCountIs(1);
        }


        @Test
        public void getShouldThrowLoanNotFoundException() {
            Assertions.assertThrows(BookNotBorrowed.class, () -> dao.get(getNonExistingLoanId()));

        }
    }

    @Nested
    public class ExistingLoan {

        @Test
        public void savingSecondTimeShouldntAddToLoans() throws BookNotBorrowed {
            dao.save(LOAN);
            assertLoanCountIs(1);
            assertEquals(LOAN, dao.get(LOAN.getBorrowedBook().getId()));
        }

        @Test
        public void deletionShouldBeSuccess() throws Exception {
            dao.delete(LOAN.getBorrowedBook().getId());
            assertLoanCountIs(0);
            assertFalse(dao.getAll().containsKey(LOAN.getBorrowedBook().getId()));
        }

        @Test
        public void getShouldReturnTheLoan() throws BookNotBorrowed {
            Loan retrievedLoan = dao.get(LOAN.getBorrowedBook().getId());
            assertEquals(LOAN, retrievedLoan);
        }
    }

    private int getNonExistingLoanId() {
        return 999;
    }

    private void assertLoanCountIs(int count) {
        try (Stream<Map.Entry<Integer, Loan>> allLoans = dao.getAll().entrySet().stream()) {
            assertEquals(count, allLoans.count());
        }
    }
}

