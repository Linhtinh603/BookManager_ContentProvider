package com.example.bookmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button bt_Exit,bt_ThongTinSach, bt_ThongTinTacGia, bt_TimKiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        bt_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_ThongTinSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BookActivity.class);
                startActivity(intent);
            }
        });
        bt_ThongTinTacGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AuthorActivity.class);
                startActivity(intent);
            }
        });
        bt_TimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void AnhXa(){
        bt_Exit = (Button)findViewById(R.id.bt_ExitMain);
        bt_ThongTinSach = findViewById(R.id.bt_ThongTinSach);
        bt_ThongTinTacGia = findViewById(R.id.bt_ThongTinTacGia);
        bt_TimKiem = findViewById(R.id.bt_TimKiem);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.mnBook:
//                Intent intent_Book = new Intent(MainActivity.this,BookActivity.class);
//                startActivity(intent_Book);
//                return true;
//            case R.id.mntttg:
//                Intent intent_tg = new Intent(MainActivity.this,AuthorActivity.class);
//                startActivity(intent_tg);
//                return true;
//            case R.id.idtimkiem:
//                Intent intent_Search = new Intent(MainActivity.this,SearchActivity.class);
//                startActivity(intent_Search);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
