package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SuaThongTinBacSi_Activity extends AppCompatActivity {
    Database database;



    ArrayList<ThongTinBacSi> mangBS;
    ThongTinBacSiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin_bac_si);
        mangBS = new ArrayList<>();

        adapter = new ThongTinBacSiAdapter(this, mangBS, true);

        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS bacsi(mabs VARCHAR(20) PRIMARY KEY, tenbs NVARCHAR(50), gioitinh NVARCHAR(20), namsinh Date, sdt varchar(15), chuyennganh NVARCHAR(200), tendn VARCHAR(20) NOT NULL, anh BLOB)");

    }
}