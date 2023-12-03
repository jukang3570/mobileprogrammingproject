package com.example.votepage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class myContentProvider extends ContentProvider {
    private SQLiteDatabase db;
    static final String PROVIDER_NAME = "com.app.first.myContentProvider";
    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;
    static final int GET_ALL = 1;
    static final int INSERT = 2;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "getAll", GET_ALL);
        uriMatcher.addURI(PROVIDER_NAME, "insert", INSERT);

    }
    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("test","uri : "+uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("my_table");
        qb.setProjectionMap(STUDENTS_PROJECTION_MAP);

        if (sortOrder == null || sortOrder == ""){
            sortOrder = /*NAME*/"contents";
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
