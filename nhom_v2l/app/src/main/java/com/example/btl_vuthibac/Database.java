package com.example.btl_vuthibac;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //truy vấn không trả kết quả
    public void QueryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);

    }
    public void QueryDulieu(String sql, byte[]... params) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(sql);

        for (int i = 0; i < params.length; i++) {
            statement.bindBlob(i + 1, params[i]); // Gán blob vào câu lệnh
        }

        statement.executeInsert(); // Hoặc executeUpdate/Delete tùy thuộc vào câu lệnh
        database.close();
    }
    //truy vấn có kết quả
    public Cursor GetData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    // Phương thức để thực hiện câu lệnh SQL với một tham số blob
    public void QueryData(String sql, byte[] param) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindBlob(1, param); // Gán blob vào câu lệnh
        statement.executeInsert();
        database.close();
    }
    //dùng trong trang thêm thông tin bác sĩ
    public void QueryData(String sql, Object... args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql, args); // Sử dụng args để truyền tham số
        db.close();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS bacsi("
                + "mabs VARCHAR(20) PRIMARY KEY, "
                + "tenbs NVARCHAR(50), "
                + "gioitinh NVARCHAR(20), "
                + "namsinh DATE, "
                + "sdt VARCHAR(15), "
                + "chuyennganh NVARCHAR(200), "
                + "tendn VARCHAR(20) NOT NULL, "
                + "anh BLOB)");
    }


    //dùng trong lichdattheouser
    public Cursor GetData(String query, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, selectionArgs);
    }
    // Dùng cho xóa đặt lịch ở đatlichvoiuser
    public void deleteData(String maDatLich) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("thongtinlichdatkham", "maso = ?", new String[]{maDatLich});
        db.close();
    }
}
