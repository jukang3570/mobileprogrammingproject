package com.example.votepage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper(@NonNull Context context) {
        super(context,"memodb",null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String memoSQL = "create table tb_memo (" +
                "_id integer primary key autoincrement,"+
                "address text," +
                "count integer)";
        db.execSQL(memoSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_memo");
            onCreate(db);
        }
    }
    public long insertAddress(String address, int count) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("count",count);

        long result = db.insert("tb_memo", null, values);

        db.close();
        return result;
    }
}
