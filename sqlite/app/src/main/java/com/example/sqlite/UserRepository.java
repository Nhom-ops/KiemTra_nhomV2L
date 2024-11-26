package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Mở kết nối đến cơ sở dữ liệu
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Đóng kết nối đến cơ sở dữ liệu
    public void close() {
        dbHelper.close();
    }

    // Thêm người dùng vào cơ sở dữ liệu
    public long addUser(String name, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);

        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    // Lấy danh sách người dùng
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                users.add(name + " - " + email);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return users;
    }

    // Cập nhật thông tin người dùng
    public int updateUser(long id, String name, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);

        return database.update(DatabaseHelper.TABLE_NAME, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Xóa người dùng
    public void deleteUser(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

