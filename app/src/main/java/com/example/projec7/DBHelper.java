package com.example.projec7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    public static final int POST_DATABASE_VERSION = 1;
    public static final int COMMENT_DATABASE_VERSION = 2; // Increment this when you modify post_db schema


    public DBHelper(@Nullable Context context) {
        super(context, "post.db", null,  POST_DATABASE_VERSION);

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
                "image_data)";

        String commentSQL = "create table comment_db (" +
                "_id integer primary key autoincrement," +
                "post_id integer," +
                "comment_text," +
                "foreign key(post_id) references post_db(_id) ON DELETE CASCADE)";
        db.execSQL(postSQL);
        db.execSQL(commentSQL);


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
    public List<Post> getFilteredPosts(String localCategory, String themeCategory) {
        List<Post> filteredPosts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "_id",
                "title",
                "content",
                "local_category",
                "theme_category",
                "image_data"
        };

        String selection;
        String[] selectionArgs;

        if ("구 선택".equals(localCategory) && "전체".equals(themeCategory)) {
            // Show all posts
            selection = null;
            selectionArgs = null;
        } else if ("구 선택".equals(localCategory)) {
            // Show posts for the selected theme category
            selection = "theme_category = ?";
            selectionArgs = new String[]{themeCategory};
        } else if ("전체".equals(themeCategory)) {
            // Show posts for the selected local category
            selection = "local_category = ?";
            selectionArgs = new String[]{localCategory};
        } else {
            // Show posts for both local and theme categories
            selection = "local_category = ? AND theme_category = ?";
            selectionArgs = new String[]{localCategory, themeCategory};
        }

        Cursor cursor = db.query(
                "post_db",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Post post = new Post();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            post.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setLocalCategory(cursor.getString(cursor.getColumnIndexOrThrow("local_category")));
            post.setThemeCategory(cursor.getString(cursor.getColumnIndexOrThrow("theme_category")));
            post.setImageData(cursor.getString(cursor.getColumnIndexOrThrow("image_data")));

            filteredPosts.add(post);
        }

        cursor.close();
        db.close();

        return filteredPosts;
    }
    public List<Comment> getCommentsForPost(int postId) {
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "_id",
                "post_id",
                "comment_text"
        };

        String selection = "post_id = ?";
        String[] selectionArgs = {String.valueOf(postId)};

        Cursor cursor = db.query(
                "comment_db",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        println("getCommentsForPost - Query result count: " + cursor.getCount()); // 로그 추가

        while (cursor.moveToNext()) {
            Comment comment = new Comment();
            comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            comment.setPostId(cursor.getInt(cursor.getColumnIndexOrThrow("post_id")));
            comment.setCommentText(cursor.getString(cursor.getColumnIndexOrThrow("comment_text")));
            comments.add(comment);
        }

        cursor.close();
        db.close();

        return comments;
    }

    public long insertComment(int postId, String commentText) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put("post_id", postId);
            values.put("comment_text", commentText);

            long newRowId = db.insert("comment_db", null, values);

            db.setTransactionSuccessful();

            return newRowId;
        } finally {
            db.endTransaction();
            db.close();
        }
    }



    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        println("onUpgrade 호출됨 : " + oldVersion + " -> " + newVersion);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        // 다운그레이드 허용 코드
    }
    public int deleteComment(int commentId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(commentId)};
        int deletedRows = db.delete("comment_db", whereClause, whereArgs);
        db.close();
        return deletedRows;
    }



}