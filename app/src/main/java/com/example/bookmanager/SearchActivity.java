package com.example.bookmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Button btn_Tim;
    ListView lv_books;
    EditText edt_Matg;
    DBHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_Tim = (Button)findViewById(R.id.btnTimSearch);
        edt_Matg = (EditText)findViewById(R.id.edtAuthorId);
        lv_books = findViewById(R.id.lv_books);

        dbhelper =  new DBHelper(this);
        btn_Tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Book> books = new ArrayList<>();
                String authorId = edt_Matg.getText().toString();
                books = dbhelper.getBooksByAuthorId(authorId);
                if(books != null){
                    ArrayAdapter adapter = new ArrayAdapter<Book>(getApplicationContext(),android.R.layout.simple_list_item_1,books);
                    lv_books.setAdapter(adapter);
                }else{
                    Toast.makeText(getApplicationContext(),"Khong tim thay",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
