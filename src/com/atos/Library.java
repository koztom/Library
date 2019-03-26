package com.atos;
import com.atos.dao.implementation.BookDAO;
import com.atos.dao.implementation.LoanDAO;
import com.atos.dao.implementation.MemberDAO;
import com.atos.exception.*;
import com.atos.model.Book;
import com.atos.model.Loan;
import com.atos.model.Member;
import com.atos.utils.DataLoader;

public class Library {

    private static Library instance;
    private static BookDAO bookDao;
    private static MemberDAO memberDao;
    private static LoanDAO loanDAO;

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
            bookDao = new BookDAO();
            memberDao = new MemberDAO();
            loanDAO = new LoanDAO();
        }
        return instance;
    }

    private Library() {
    }

    public void removeBook(int bookId) throws BookNotFoundException, BookCurrentlyBorrowed {
        if (bookDao.get(bookId).getAvailable()){
            bookDao.delete(bookId);
        } else throw new BookCurrentlyBorrowed(bookId);
    }

    public Book createBook(String title,String author,int year){
        Book newBook = new Book(title,author,year);
        bookDao.save(newBook);
        return newBook;
    }

    private Member createMember(String name){
        Member newMember = new Member(name);
        memberDao.save(newMember);
        return newMember;
    }

    public void loadExampleData(){
        bookDao.getAll().putAll(DataLoader.loadBooksData());
        memberDao.getAll().putAll(DataLoader.loadMembersData());
    }

    public void borrowBook(int bookToBorrowId, String borrower) throws BookNotFoundException, BookCurrentlyBorrowed {
        Member look = findMember(borrower);
        Book book = bookDao.get(bookToBorrowId);
        if(!book.getAvailable()){
            throw new BookCurrentlyBorrowed(bookToBorrowId);
        } else {
            book.setAvailable(false);
            Loan loan = new Loan(book, look);
            loanDAO.getAll().put(book.getId(), loan);
        }

    }

    public void returnBook(int bookId) throws BookNotFoundException, BookNotBorrowed {
        if (bookDao.get(bookId).getAvailable()){
            throw new BookNotBorrowed(bookId);
        } else {
            loanDAO.get(bookId).getBorrowedBook().setAvailable(true);
            loanDAO.delete(bookId);
        }
    }

    public Member findMember(String name){
        Member sought;
        try {
            sought = memberDao.getByName(name);
        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            System.out.println("Creating new member:");
            sought = createMember(name);
            System.out.println(sought.toString());
        }
        return sought;
    }

    public void listAllBooks(){
        bookDao.getAll().forEach((k,v) -> System.out.println("Book:"+v));
        System.out.println("There are "+bookDao.getAll().size()+" books, "+loanDAO.getAll().size()+" among them have been borrowed");
    }

    public void getBookDetails(int bookId) throws BookNotFoundException, BookNotBorrowed {
        Book book = bookDao.get(bookId);
        System.out.println("Book:"+book.toString());
        if (!book.getAvailable()){
            System.out.println("Borrowed by: "+loanDAO.get(bookId).getBorrower().getName());
        }
    }

     public void findBookByParameters(String author, String title, String yearString) throws NoBookFound {
        bookDao.findBookByParameters(author, title, yearString).forEach(System.out::println);
     }


}
