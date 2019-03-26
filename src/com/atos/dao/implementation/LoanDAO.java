package com.atos.dao.implementation;

import com.atos.dao.Dao;
import com.atos.exception.BookNotBorrowed;
import com.atos.model.Loan;

import java.util.HashMap;
import java.util.Map;

public class LoanDAO implements Dao<Loan> {

    private Map<Integer, Loan> borrowed = new HashMap<>();

    public Loan get(Integer id) throws BookNotBorrowed {
        Loan loan = borrowed.get(id);
        if (loan == null) {
            throw new BookNotBorrowed(id);
        }
        return loan;
    }

    public Map<Integer, Loan> getAll() {
        return borrowed;
    }

    public void save(Loan newLoan) {
        borrowed.put(newLoan.getBorrowedBook().getId(), newLoan);
    }

    public void delete(Integer id) throws BookNotBorrowed {
        if(borrowed.containsKey(id)){
            borrowed.remove(id);
        } else throw new BookNotBorrowed(id);
    }
}
