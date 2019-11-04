package com.example.bookmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    EditText et_idbook, et_title, et_idauthor;
    Button bt_save, bt_cancle, bt_exit, bt_update, bt_delete;
    Spinner spinnerAuthor;
    ListView lv_display;
    DBHelper dbHelper;
    ArrayAdapter<Book> adapter;
    ArrayAdapter<Author> authorArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        AnhXa();
        showBook();
        bt_delete.setEnabled(false);
        bt_update.setEnabled(false);

//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Book book = new Book();
//                book.setId_Book(Integer.parseInt(et_idbook.getText().toString()));
//                book.setTitle(et_title.getText().toString());
//                book.setId_Author(Integer.parseInt(et_idbook.getText().toString()));
//                if(dbHelper.InsertBook(book)>0){
//                    Toast.makeText(BookActivity.this,"Ban da them thanh cong",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(BookActivity.this,"Ban them khong thanh cong",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        bt_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<Book> list_book = new ArrayList<>();
//                ArrayList<String> list_string = new ArrayList<>();
//                list_book = dbHelper.getAllBook();
//                for(Book book : list_book){
//                    list_string.add(book.getId_Book()+"");
//                    list_string.add(book.getTitle());
//                    list_string.add(book.getId_Author()+"");
//                }
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BookActivity.this,android.R.layout.simple_list_item_1,list_string);
//                gv_display.setAdapter(arrayAdapter);
//            }
//        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id_book",et_idbook.getText().toString());
                contentValues.put("title", et_title.getText().toString());
//                contentValues.put("id_author",et_idauthor.getText().toString());
                Author author = (Author) spinnerAuthor.getSelectedItem();
                int authorId = author.getId_Author();
                contentValues.put("id_author",authorId);
                try {
                    Uri insert_uri = getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
                    Toast.makeText(getApplicationContext(),"Lưu thành công",Toast.LENGTH_SHORT).show();
                    et_idbook.getText().clear();
                    et_title.getText().clear();
                    et_idauthor.getText().clear();
                    showBook();
                }catch (SQLException ex){
                    Toast.makeText(getApplicationContext(),"Lưu không thành công",Toast.LENGTH_SHORT).show();
                }

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultActivity();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_idbook.getText().toString();
                String uri = MyContentProvider.URL+"/"+id;
                Uri content_uri = Uri.parse(uri);
                ContentValues values = new ContentValues();
                values.put("title", et_title.getText().toString());
//                values.put("id_author", Integer.parseInt(et_idauthor.getText().toString()));
                Author author = (Author) spinnerAuthor.getSelectedItem();
                int authorId = author.getId_Author();
                values.put("id_author",authorId);
                int updated = getContentResolver().update(content_uri,values,null,null);
                if( updated > 0 ){
                    Toast.makeText(getApplicationContext(),"Cập nhật dữ liệu thành công",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cập nhật không thành công",Toast.LENGTH_SHORT).show();
                }
                lv_display.deferNotifyDataSetChanged();
                setDefaultActivity();
                showBook();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDelete();
            }
        });

        lv_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bt_save.setEnabled(false);
                Book book = (Book) lv_display.getItemAtPosition(i);
                Log.v("Book", book.toString());
                et_idbook.setText(String.valueOf(book.getId_Book()));
                et_title.setText(book.getTitle());
//                et_idauthor.setText(String.valueOf(book.getId_Author()));
                Author author = dbHelper.getAuthorById(String.valueOf(book.getId_Author()));
                int authorPos =  authorArrayAdapter.getPosition(author);
                Log.v("author",author.toString());
                Log.v("pos",String.valueOf(authorPos));
                spinnerAuthor.setAdapter(authorArrayAdapter);
                spinnerAuthor.setSelection(1,false);
                bt_delete.setEnabled(true);
                bt_update.setEnabled(true);
                bt_cancle.setEnabled(true);
                et_idbook.setEnabled(false);
            }
        });
    }
    private void AnhXa(){
        et_idbook = (EditText)findViewById(R.id.edtMaSo);
        et_title = (EditText)findViewById(R.id.edtTieuDe);
        et_idauthor = (EditText)findViewById(R.id.edtIdTacGia);
        bt_save = (Button)findViewById(R.id.btnSave);
        bt_delete = (Button)findViewById(R.id.btnDelete);
        bt_cancle = (Button)findViewById(R.id.btnCancle);
        bt_update = (Button)findViewById(R.id.btnUpdate);
        bt_exit = (Button)findViewById(R.id.btnExit);
        spinnerAuthor = findViewById(R.id.spinner);
        dbHelper = new DBHelper(this);

        ArrayList<Author> authors = new ArrayList<>();
        authors = dbHelper.getAllAuthor();
        authorArrayAdapter = new ArrayAdapter<Author>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,authors);
        spinnerAuthor.setAdapter(authorArrayAdapter);
        lv_display = findViewById(R.id.lv_display);
    }

    private void showBook(){
        ArrayList<Book> list_string = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,"title");
        if(cursor!=null && cursor.moveToFirst()){
            do{
                Book book = new Book(cursor.getInt(0),cursor.getString(1), cursor.getInt(2) );
                list_string.add(book);
            }while (cursor.moveToNext());
            adapter = new ArrayAdapter<Book>(getApplicationContext(),android.R.layout.simple_list_item_1,list_string);
            lv_display.setAdapter(adapter);
            cursor.close();
        }
        else
            Toast.makeText(getApplicationContext(),"Không có dữ liệu",Toast.LENGTH_SHORT).show();
    }

    private void setDefaultActivity(){
        bt_update.setEnabled(false);
        bt_delete.setEnabled(false);
        bt_cancle.setEnabled(false);
        bt_save.setEnabled(true);
        et_idbook.getText().clear();
        et_title.getText().clear();
        et_idauthor.getText().clear();
    }

    private void confimDelete(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn chắc chắn có muốn xóa ?");
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDefaultActivity();
            }
        });

        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id = et_idbook.getText().toString();
                String uri = MyContentProvider.URL+"/"+id;
                Uri content_uri = Uri.parse(uri);
                int deleted = getContentResolver().delete(content_uri,null,null);
                if(deleted > 0 ){
                    Toast.makeText(getApplicationContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Xóa không thành công",Toast.LENGTH_SHORT).show();
                }
                lv_display.deferNotifyDataSetChanged();
                setDefaultActivity();
                showBook();
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }

}
