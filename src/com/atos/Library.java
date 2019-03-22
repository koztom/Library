package com.atos;

import com.atos.exception.BookCurrentlyBorrowed;
import com.atos.exception.BookNotBorrowed;
import com.atos.exception.BookNotFoundException;
import com.atos.model.Book;
import com.atos.model.Loan;
import com.atos.model.Member;
import com.atos.utils.DataLoader;

import java.util.*;
import java.util.stream.Collectors;

public class Library {

    private static Library instance;
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Loan> borrowed = new HashMap<>();
    private List<Member> members = new ArrayList<>();

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }

        return instance;
    }

    private Library() {
    }

    public void removeBook(int bookId) throws BookNotFoundException, BookCurrentlyBorrowed {
        if (findBookById(bookId).getAvailable()){
            books.remove(bookId);
        } else throw new BookCurrentlyBorrowed(bookId);
    }

    public Book createBook(String title,String author,int year){
        Book newBook = new Book(title,author,year);
        books.put(newBook.getId(), newBook);
        return newBook;
    }

    public Member createMember(String name){
        Member newMember = new Member(name);
        members.add(newMember);
        return newMember;
    }

    public void loadExampleData(){
        books = DataLoader.loadBooksData();
        members = DataLoader.loadMembersData();
    }

    public Map<Integer, Book> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Integer, Book> books) {
        this.books = books;
    }

    public void borrowBook(int bookToBorrowId, String borrower) throws BookNotFoundException, BookCurrentlyBorrowed {
        Member look = findMember(borrower);
        Book book = findBookById(bookToBorrowId);
        if(!book.getAvailable()){
            throw new BookCurrentlyBorrowed(bookToBorrowId);
        } else {
            book.setAvailable(false);
            Loan loan = new Loan(book, look);
            borrowed.put(book.getId(), loan);
        }

    }

    public void returnBook(int bookId) throws BookNotFoundException, BookNotBorrowed {
        if(findBookById(bookId).getAvailable()){
            throw new BookNotBorrowed(bookId);
        } else {
            borrowed.remove(bookId).getBorrowedBook().setAvailable(true);
        }
    }

    public Book findBookById(int bookId) throws BookNotFoundException {
        Book bookToBorrow = books.get(bookId);
        if(bookToBorrow==null){
            throw new BookNotFoundException(bookId);
        }
        return bookToBorrow;
    }
    public Member findMember(String name){
        Member sought = members.stream()
                .filter(member -> name.equals(member.getName()))
                .findAny()
                .orElse(null);
        if(sought == null){
            System.out.println("No member found, creating new member:");
            sought = createMember(name);
            System.out.println(sought.toString());
        }
        return sought;
    }

    public void listAllBooks(){
        books.forEach((k,v) -> System.out.println("Book:"+v));
        System.out.println("There are "+books.size()+" books, "+borrowed.size()+" among them have been borrowed");
    }

    public void getBookDetails(int bookId) throws BookNotFoundException {
        Book book = findBookById(bookId);
        System.out.println("Book:"+book.toString());
        if (!book.getAvailable()){
            System.out.println("Borrowed by: "+borrowed.get(bookId).getBorrower().getName());
        }
    }

     public void findBookByParameters(String author, String title, String yearString){
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
             while(true){
                 try{
                     int year = Integer.parseInt(yearString);
                     filteredBooks = filteredBooks
                             .stream()
                             .filter(x -> x.getYear()==year)
                             .collect(Collectors.toList());
                     searched = true;
                     break;
                 } catch (NumberFormatException e) {
                     System.out.println("Invalid Input, enter value again");
                     Scanner input = new Scanner(System.in);
                     yearString = input.nextLine();
                 }
             }
         }
         if(searched){
             filteredBooks.forEach(System.out::println);
         } else System.out.println("No book was found");
     }


}
