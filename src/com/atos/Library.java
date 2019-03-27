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
    private final BookDAO bookDao;
    private final MemberDAO memberDao;
    private final LoanDAO loanDAO;

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();

        }
        return instance;
    }

    private Library() {
        bookDao = new BookDAO();
        memberDao = new MemberDAO();
        loanDAO = new LoanDAO();
    }

    public BookDAO getBookDao() {
        return bookDao;
    }


    public void removeBook(int bookId) throws BookNotFoundException, BookCurrentlyBorrowed {
        if (getBookDao().get(bookId).getAvailable()){
            getBookDao().delete(bookId);
        } else throw new BookCurrentlyBorrowed(bookId);
    }

    public Book createBook(String title,String author,int year){
        Book newBook = new Book(title,author,year);
        getBookDao().save(newBook);
        return newBook;
    }

    private Member createMember(String name){
        Member newMember = new Member(name);
        getMemberDao().save(newMember);
        return newMember;
    }

    public void loadExampleData(){
        getBookDao().getAll().putAll(DataLoader.loadBooksData());
        getMemberDao().getAll().putAll(DataLoader.loadMembersData());
    }

    public void borrowBook(int bookToBorrowId, String borrower) throws BookNotFoundException, BookCurrentlyBorrowed {
        Member look = findMember(borrower);
        Book book = getBookDao().get(bookToBorrowId);
        if(!book.getAvailable()){
            throw new BookCurrentlyBorrowed(bookToBorrowId);
        } else {
            book.setAvailable(false);
            Loan loan = new Loan(book, look);
            getLoanDAO().getAll().put(book.getId(), loan);
        }

    }

    public void returnBook(int bookId) throws BookNotFoundException, BookNotBorrowed {
        if (getBookDao().get(bookId).getAvailable()){
            throw new BookNotBorrowed(bookId);
        } else {
            getLoanDAO().get(bookId).getBorrowedBook().setAvailable(true);
            getLoanDAO().delete(bookId);
        }
    }

    public Member findMember(String name){
        Member sought;
        try {
            sought = getMemberDao().getByName(name);
        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            System.out.println("Creating new member:");
            sought = createMember(name);
            System.out.println(sought.toString());
        }
        return sought;
    }

    public void listAllBooks(){
        getBookDao().getAll().forEach((k, v) -> System.out.println("Book:"+v));
        System.out.println("There are "+ getBookDao().getAll().size()+" books, "+ getLoanDAO().getAll().size()+" among them have been borrowed");
    }

    public void getBookDetails(int bookId) throws BookNotFoundException, BookNotBorrowed {
        Book book = getBookDao().get(bookId);
        System.out.println("Book:"+book.toString());
        if (!book.getAvailable()){
            System.out.println("Borrowed by: "+ getLoanDAO().get(bookId).getBorrower().getName());
        }
    }

     public void findBookByParameters(String author, String title, String yearString) throws NoBookFound {
        getBookDao().findBookByParameters(author, title, yearString).forEach(System.out::println);
     }


    public MemberDAO getMemberDao() {
        return memberDao;
    }

    public LoanDAO getLoanDAO() {
        return loanDAO;
    }
}
