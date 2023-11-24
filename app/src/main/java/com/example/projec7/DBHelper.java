package com.example.projec7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, "database", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String postSQL = "create table post_db (" +
                "_id integer primary key autoincrement," +
                "title," +
                "content," +
                "local_category," +
                "theme_category," +
                "image_data)";
        db.execSQL(postSQL);

    }

    public void insertPost(String title, String content, String localCategory, String themeCategory, String imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("local_category", localCategory);
        values.put("theme_category", themeCategory);
        values.put("image_data", imageData);

        long newRowId = db.insert("post_db", null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            // Log the ID of the newly inserted row
            Log.d("DBHelper", "New row ID: " + newRowId);
        } else {
            // Log an error if the insertion failed
            Log.e("DBHelper", "Error inserting row");
        }


    }




    // Base64 문자열을 Bitmap으로 변환하는 메서드
    private Bitmap convertBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String updateQuery = "ALTER TABLE tb_post ADD COLUMN local_category TEXT";
            db.execSQL(updateQuery);
        }
    }
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("post_db", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String localCategory = cursor.getString(cursor.getColumnIndex("local_category"));
                String themeCategory = cursor.getString(cursor.getColumnIndex("theme_category"));
                String imageData = cursor.getString(cursor.getColumnIndex("image_data"));

                posts.add(new Post(title, content, localCategory, themeCategory, imageData));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return posts;
    }

}