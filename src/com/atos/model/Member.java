package com.atos.model;

public class Member {

    private String name;
    private int id;
    private static int counter = 0;

    public Member(String name){
        this.setId(counter++);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return " Name: "+getName()+" Id: "+getId();
    }
}
