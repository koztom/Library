package com.atos.model;

public class Book {

    private String title;
    private String author;
    private int year;
    private int id;
    private boolean isAvailable;
    private static int counter = 0;

    public Book(String title,String author,int year){
        this.setId(counter++);
        this.setTitle(title);
        this.setAuthor(author);
        this.setYear(year);
        this.setAvailable(true);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String toString(){
        return "ID: "+getId()+" Title: "+getTitle()+" Author: "+getAuthor()+" Year: "+getYear()+" Is available: "+getAvailable();
    }
}
