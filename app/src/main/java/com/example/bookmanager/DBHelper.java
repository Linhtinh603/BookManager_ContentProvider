package com.example.bookmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    DBHelper(Context context){
        super(context,"BookDatabase_sqlite",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table Authors ("+"id_author integer primary key ,"+"name text,"+"address text,"+"email text)");
        sqLiteDatabase.execSQL("Create Table Books("+
                "id_book integer primary key,"
                +"title text,"+"id_author integer "
                +" constraint id_author references Authors(id_author)"+"On delete Cascade on Update CasCade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists Books");
        sqLiteDatabase.execSQL("Drop table if exists Authors");
        onCreate(sqLiteDatabase);
    }

    public int InsertAuthor(Author author){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_author", author.getId_Author());
        contentValues.put("name", author.getName());
        contentValues.put("address", author.getAddress());
        contentValues.put("email", author.getEmail());
        int result = (int)db.insert("Authors",null,contentValues);
        return  result;
    }


    public ArrayList<Author> getAllAuthor(){
        ArrayList<Author> listAuthor = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Authors",null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        while (cursor.isAfterLast()==false){
            listAuthor.add(new Author(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return  listAuthor;
    }

    public Author getAuthorById(String authorId) {
        Author author = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Authors where id_author = ?",new String[]{authorId});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String email = cursor.getString(3);
            author = new Author();
            author.setId_Author(id);
            author.setName(name);
            author.setAddress(address);
            author.setEmail(email);

        }
        cursor.close();
        db.close();
        return author;
    }

    public boolean updateAuthor(Author author){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", author.getName());
        contentValues.put("address", author.getAddress());
        contentValues.put("email", author.getEmail());

        int updated = db.update("Authors",contentValues,"id_author"+ "=" + author.getId_Author(),null);
        if(updated > 0){
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public boolean deleteAuthor(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete("Authors","id_author"+ "=?", new String[]{id});
        if(deleted > 0){
            db.close();
            return  true;
        };
        db.close();
        return false;
    }


    public int InsertBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_book", book.getId_Book());
        contentValues.put("title", book.getTitle());
        contentValues.put("id_author", book.getId_Author());
        int result = (int)db.insert("Books",null,contentValues);
        db.close();
        return  result;
    }

    public ArrayList<Book> getAllBook(){
        ArrayList<Book> listBook = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Books",null);
        if(cursor!=null && cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                listBook.add(new Book(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return  listBook;
    }

    public Book getBookById(String bookId) {
        Book book = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Books where id_book = ?",new String[]{bookId});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id_Book = cursor.getInt(0);
            String title = cursor.getString(1);
            int id_Author = cursor.getInt(2);
            book.setId_Book(id_Book);
            book.setTitle(title);
            book.setId_Author(id_Author);
        }
        cursor.close();
        db.close();
        return book;
    }

    public ArrayList<Book> getBooksByAuthorId(String authorId){
        ArrayList<Book> listBook = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Books b  WHERE b.id_author = ?",
                new String[]{authorId});

        if(cursor != null &&  cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                listBook.add(new Book(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)));
                cursor.moveToNext();
            }
        }else{
            return null;
        }
        cursor.close();
        db.close();
        return listBook;
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE product SET name=?, price = ? where id = ?",
                new String[]{book.getId_Book() + "", book.getTitle() + "", book.getId_Author() + ""});
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", book.getTitle());
        contentValues.put("id_author", book.getId_Author());
        db.update("Books",contentValues,"id_book = ?",null);

        db.close();
    }

}
