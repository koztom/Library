package com.atos.utils;

import com.atos.model.Book;
import com.atos.model.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public final class DataLoader {

    private DataLoader (){

    }

    public static Map<Integer, Book> loadBooksData(){

        Map<Integer, Book> booksLoaded = new HashMap<>();
        try {
            File file = new File(ClassLoader.class.getResource("/com/atos/resources/books_example_data").getFile());
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] params = sc.nextLine().split(",");
                Book newBook = new Book(params[0], params[1], Integer.parseInt(params[2]));
                booksLoaded.put(newBook.getId(),newBook);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return booksLoaded;
    }

    public static Map<Integer,Member> loadMembersData(){

        Map<Integer,Member> membersLoaded = new HashMap<>();
        try {
            File file = new File(ClassLoader.class.getResource("/com/atos/resources/members_example_data").getFile());
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String name = sc.nextLine();
                Member newMember = new Member(name);
                membersLoaded.put(newMember.getId(),newMember);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return membersLoaded;
    }
}
