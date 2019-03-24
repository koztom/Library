package com.atos;

import com.atos.exception.BookCurrentlyBorrowed;
import com.atos.exception.BookNotBorrowed;
import com.atos.exception.BookNotFoundException;
import com.atos.exception.NoBookFound;
import com.atos.model.Book;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Library lib = Library.getInstance();
        boolean run = true;
        Scanner input= new Scanner(System.in);
        System.out.println("Welcome to Library");
        while(run){
            System.out.println("Choose what to do:");
            System.out.println("1- Load example data");
            System.out.println("2- Add a book");
            System.out.println("3- Remove a book");
            System.out.println("4- List all books");
            System.out.println("5- Find book details");
            System.out.println("6- Borrow a book");
            System.out.println("7- Return a book");
            System.out.println("8- Search with parameters");
            System.out.println("9- Exit");
            int num = takeNumberInput();
            switch (num) {
                case 1:
                    lib.loadExampleData();
                    break;
                case 2:
                    System.out.println("Enter author");
                    String author= input.nextLine();

                    System.out.println("Enter title");
                    String title= input.nextLine();
                    System.out.println("Enter year");
                    int year = takeNumberInput();
                    Book newBook = lib.createBook(title,author,year);
                    System.out.println("Book has been created: "+newBook.toString());
                    break;
                case 3:
                    System.out.println("Enter book ID you want to remove");
                    int removeBookId = takeNumberInput();
                    try {
                        lib.removeBook(removeBookId);
                    } catch (BookNotFoundException | BookCurrentlyBorrowed bookException) {
                        bookException.printStackTrace();
                    }
                    break;
                case 4:
                    lib.listAllBooks();
                    break;
                case 5:
                    System.out.println("Enter book ID you are looking for");
                    int findBookId = takeNumberInput();
                    try {
                        lib.getBookDetails(findBookId);
                    } catch (BookNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("Enter borrower name");
                    String name= input.nextLine();
                    System.out.println("Enter book ID you want to borrow");
                    int borrowBookId = takeNumberInput();
                    try {
                        lib.borrowBook(borrowBookId,name);
                    } catch (BookNotFoundException | BookCurrentlyBorrowed bookException) {
                        bookException.printStackTrace();
                    }
                    break;
                case 7:
                    System.out.println("Enter book ID you want to return");
                    int returnBookId = takeNumberInput();
                    try {
                        lib.returnBook(returnBookId);
                    } catch (BookNotFoundException | BookNotBorrowed bookException) {
                        bookException.printStackTrace();
                    }
                    break;
                case 8:
                    System.out.println("Enter parameters you want to look book for (press enter to skip)");
                    System.out.println("Searched author:");
                    String searchedAuthor= input.nextLine();
                    System.out.println("Searched title:");
                    String searchedTitle= input.nextLine();
                    System.out.println("Searched year:");
                    String searchedYear= input.nextLine();
                    //int searchedYear = input.nextInt();
                   // lib.findBookByParameters(searchedAuthor,searchedTitle,searchedYear);
                    while(true) {
                        try {
                            lib.findBookByParameters(searchedAuthor, searchedTitle, searchedYear);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Input, enter value again");
                            searchedYear = input.nextLine();
                        } catch (NoBookFound noBookFound) {
                            noBookFound.printStackTrace();
                            break;
                        }
                    }
                            break;
                case 9:
                    run = false;
                    break;
                default: {
                    System.out.println("Wrong value");
                }
            }
        }

    }

    private static int takeNumberInput() {
        Scanner input = new Scanner(System.in);
        while(true) {
            String choice = input.nextLine();
            try {
                return Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input, enter value again");
            }
        }
    }
}
