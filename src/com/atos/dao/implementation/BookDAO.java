package com.atos.dao.implementation;

import com.atos.dao.Dao;
import com.atos.exception.BookNotFoundException;
import com.atos.exception.NoBookFound;
import com.atos.model.Book;

import java.util.*;
import java.util.stream.Collectors;

public class BookDAO implements Dao<Book> {

    private Map<Integer, Book> books = new HashMap<>();

    public Book get(Integer id) throws BookNotFoundException {
        Book book = books.get(id);
        if(book==null){
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public Map<Integer, Book> getAll() {
        return books;
    }

    public void save(Book newBook) {
        books.put(newBook.getId(), newBook);
    }

    public void delete(Integer id) {
        books.remove(id);
    }

    public List<Book> findBookByParameters(String author, String title, String yearString) throws NoBookFound {
        List<Book> filteredBooks = new ArrayList<>(books.values());
        boolean searched = false;
        if(!author.isEmpty()){
            filteredBooks = filteredBooks
                    .stream()
                    .filter(x -> x.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
            searched = true;
        }
        if(!title.isEmpty()){
            filteredBooks = filteredBooks
                    .stream()
                    .filter(x -> x.getTitle().equalsIgnoreCase(title))
                    .collect(Collectors.toList());
            searched = true;
        }
        if(!yearString.isEmpty()){
            int year = Integer.parseInt(yearString);
            filteredBooks = filteredBooks
                    .stream()
                    .filter(x -> x.getYear()==year)
                    .collect(Collectors.toList());
            searched = true;
        }
        if(searched){
           return filteredBooks;
        } else throw new NoBookFound();
    }
}
