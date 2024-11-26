package com.example.sqlite;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private ListView listView;
    private EditText editTextName, editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các View
        userRepository = new UserRepository(this);
        listView = findViewById(R.id.listView);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        Button addButton = findViewById(R.id.addButton);

        // Mở kết nối đến cơ sở dữ liệu
        userRepository.open();

        // Thiết lập sự kiện cho nút thêm người dùng
        addButton.setOnClickListener(v -> {
            // Lấy thông tin từ EditText
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            // Kiểm tra thông tin nhập vào có hợp lệ hay không
            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thêm người dùng vào cơ sở dữ liệu
            long id = userRepository.addUser(name, email);
            if (id != -1) {
                Toast.makeText(MainActivity.this, "User added", Toast.LENGTH_SHORT).show();
                updateUserList();
                // Xóa dữ liệu trong EditText sau khi thêm người dùng
                editTextName.setText("");
                editTextEmail.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Error adding user", Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị danh sách người dùng ban đầu
        updateUserList();
    }

    // Cập nhật danh sách người dùng trong ListView
    private void updateUserList() {
        List<String> users = userRepository.getAllUsers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu
        userRepository.close();
    }
}
