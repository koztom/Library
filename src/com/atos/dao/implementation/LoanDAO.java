package com.atos.dao.implementation;

import com.atos.dao.Dao;
import com.atos.model.Loan;

import java.util.HashMap;
import java.util.Map;

public class LoanDAO implements Dao<Loan> {

    private Map<Integer, Loan> borrowed = new HashMap<>();

    public Loan get(Integer id) {
        return borrowed.get(id);
    }

    public Map<Integer, Loan> getAll() {
        return borrowed;
    }

    public void save(Loan newLoan) {
        borrowed.put(newLoan.getBorrowedBook().getId(), newLoan);
    }

    public void delete(Integer id) {
        borrowed.remove(id);
    }
}
