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
        super(context, "post.db", null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        println("onCreate 호출됨");
        String postSQL = "create table post_db (" +
                "_id integer primary key autoincrement," +
                "title," +
                "content," +
                "local_category," +
                "theme_category," +
                "image_data,"+"comment)";
        db.execSQL(postSQL);


    }
    public void onOpen(SQLiteDatabase db){
        println("onOpen 호출됨");
    }

    public void println(String data){
        Log.d("DatabaseHelper" , data);
    }
    public long insertPost(String title, String content, String localCategory, String themeCategory, String imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("local_category", localCategory);
        values.put("theme_category", themeCategory);
        values.put("image_data", imageData);
        String tableName="post_db";

        long newRowId = db.insert(tableName, null, values);

        return newRowId;
    }
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "_id",
                "title",
                "content",
                "local_category",
                "theme_category",
                "image_data"
        };

        Cursor cursor = db.query(
                "post_db",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Post post = new Post();


            post.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setLocalCategory(cursor.getString(cursor.getColumnIndexOrThrow("local_category" )));
            post.setThemeCategory(cursor.getString(cursor.getColumnIndexOrThrow("theme_category" )));
            post.setImageData(cursor.getString(cursor.getColumnIndexOrThrow( "image_data")));

            postList.add(post);
        }

        cursor.close();
        db.close();

        return postList;
    }
    public void addCommentForPost(int postId,String commentText){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("_id",postId);
        values.put("comment",commentText);
        db.insert("post_db",null,values);
        db.close();
    }






    // Base64 문자열을 Bitmap으로 변환하는 메서드
    private Bitmap convertBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        println("onUpgrade 호출됨 : " +oldVersion+" -> "+ newVersion);
        if (oldVersion>1) {
            String updateQuery = "DROP TABLE IF EXISTS "+"post_db";
            db.execSQL(updateQuery);
        }
    }


}