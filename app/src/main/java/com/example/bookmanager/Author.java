package com.example.bookmanager;

public class Author {
    private int id_Author;
    private String name;
    private String address;
    private String email;

    Author(){
        this.id_Author = 0;
        this.name = null;
        this.address = null;
        this.email = null;
    }

    public Author(int id_Author, String name, String address, String email) {
        this.id_Author = id_Author;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public int getId_Author() {
        return id_Author;
    }

    public void setId_Author(int id_Author) {
        this.id_Author = id_Author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Author: " + id_Author + " - " + name + " - " + address + " - " + email;
    }
}
