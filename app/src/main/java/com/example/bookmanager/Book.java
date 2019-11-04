package com.example.bookmanager;

public class Book {
    private int id_Book;
    private String title;
    private int id_Author;

    Book(){
        this.id_Book = 0;
        this.title = null;
        this.id_Author = 0;
    }


    public Book(int id_Book, String title, int id_Author) {
        this.id_Book = id_Book;
        this.title = title;
        this.id_Author = id_Author;
    }

    public int getId_Book() {
        return id_Book;
    }

    public void setId_Book(int id_Book) {
        this.id_Book = id_Book;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId_Author() {
        return id_Author;
    }

    public void setId_Author(int id_Author) {
        this.id_Author = id_Author;
    }

    @Override
    public String toString() {
        return id_Book + " - " + title + " - " + id_Author;
    }
}
