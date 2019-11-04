package com.example.bookmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AuthorActivity extends AppCompatActivity {
    EditText edt_IdTacGia, edt_TenTacGia, edt_DiaChi, edt_Email;
    Button btn_Update, btn_Cancle, btn_Delete, btn_Exit, btn_Save;
    ListView lv_Authors;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        dbHelper = new DBHelper(AuthorActivity.this);
        AnhXa();
        showAuthors();
        btn_Delete.setEnabled(false);
        btn_Update.setEnabled(false);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        btn_Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultActivity();
            }
        });

        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        lv_Authors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btn_Save.setEnabled(false);
                Author author = (Author) lv_Authors.getItemAtPosition(i);
                edt_IdTacGia.setText(String.valueOf(author.getId_Author()));
                edt_TenTacGia.setText(author.getName());
                edt_DiaChi.setText(author.getAddress());
                edt_Email.setText(author.getEmail());
                btn_Delete.setEnabled(true);
                btn_Update.setEnabled(true);
                btn_Cancle.setEnabled(true);
                edt_IdTacGia.setEnabled(false);
            }
        });
    }

    private void save(){
        Author author = new Author();
        System.out.println(edt_IdTacGia.getText().toString());
        author.setId_Author(Integer.parseInt(edt_IdTacGia.getText().toString()));
        author.setName(edt_TenTacGia.getText().toString());
        author.setEmail(edt_Email.getText().toString());
        author.setAddress(edt_DiaChi.getText().toString());
        System.out.println(author.toString());
        try {
            if(dbHelper.InsertAuthor(author)>0){
                Toast.makeText(AuthorActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AuthorActivity.this,"Thêm không thành công",Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException ex){
            Toast.makeText(AuthorActivity.this,"Lưu không thành công, hãy kiểm tra lại",Toast.LENGTH_SHORT).show();
        }

    }

    private void update(){
        int id = Integer.parseInt(edt_IdTacGia.getText().toString());
        String name = edt_TenTacGia.getText().toString();
        String address = edt_DiaChi.getText().toString();
        String email = edt_Email.getText().toString();
        Author author = new Author(id, name, address, email);
        if(dbHelper.updateAuthor(author)){
            Toast.makeText(getApplicationContext(),"Cập nhật dữ liệu thành công",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Cập nhật không thành công",Toast.LENGTH_SHORT).show();
        }

        lv_Authors.deferNotifyDataSetChanged();
        setDefaultActivity();
        showAuthors();
    }

    private void delete(){
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
                String id = edt_IdTacGia.getText().toString();
                boolean deleted = dbHelper.deleteAuthor(id);
                if(deleted){
                    Toast.makeText(getApplicationContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Xóa không thành công",Toast.LENGTH_SHORT).show();
                }
                lv_Authors.deferNotifyDataSetChanged();
                setDefaultActivity();
                showAuthors();
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();

    }

    private void AnhXa(){
        edt_IdTacGia = (EditText)findViewById(R.id.edtMaTG);
        edt_DiaChi = (EditText)findViewById(R.id.edtDiaChi);
        edt_Email = (EditText)findViewById(R.id.edtEmail);
        edt_TenTacGia = (EditText)findViewById(R.id.edtHoTen);
        btn_Cancle = (Button)findViewById(R.id.btnCancle);
        btn_Save = (Button)findViewById(R.id.btnSave);
        btn_Delete = findViewById(R.id.btnDelete);
        btn_Update = findViewById(R.id.btnUpdate);
        lv_Authors = findViewById(R.id.lv_authors);
    }

    private void setDefaultActivity(){
        btn_Update.setEnabled(false);
        btn_Delete.setEnabled(false);
        btn_Cancle.setEnabled(false);
        btn_Save.setEnabled(true);
        edt_IdTacGia.getText().clear();
        edt_TenTacGia.getText().clear();
        edt_DiaChi.getText().clear();
        edt_Email.getText().clear();
    }

    private void showAuthors(){
        ArrayList<Author> listAuthor = new ArrayList<>();
        listAuthor = dbHelper.getAllAuthor();
        ArrayAdapter<Author> arrayAdapter = new ArrayAdapter<>(AuthorActivity.this,android.R.layout.simple_list_item_1,listAuthor);
        lv_Authors.setAdapter(arrayAdapter);
    }

}
