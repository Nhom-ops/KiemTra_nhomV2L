package com.example.baicanhan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button okButton = findViewById(R.id.okButton);
        CheckBox rememberChoice = findViewById(R.id.rememberChoice);

        okButton.setOnClickListener(v -> {
            if (rememberChoice.isChecked()) {
                Toast.makeText(this, "Choice remembered!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(MainActivity.this, secondscreen.class);
            startActivity(intent);
        });
    }



}