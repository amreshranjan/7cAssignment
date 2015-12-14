package com.example.amreshranjan.sevencapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Amresh.ranjan on 14-12-2015.
 */
public class ContentProviderExample extends ContentProvider {

    private MainDatabaseHelper mOpenHelper;
    private static final String DBNAME = "Climate";
    private SQLiteDatabase db;

    static final String PROVIDER_NAME = "com.example.amreshranjan.sevencapplication.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/weathers";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String _ID = "_id";
    static final String DATE = "date";
    static final String TEMP = "temp";
    static final String WEATHER_TABLE_NAME = "weathers";
    private static HashMap<String, String> WEATHER_PROJECTION_MAP;
    static final int WEATHERS = 1;
    static final int WEATHERS_ID = 2;
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "weathers", WEATHERS);
        uriMatcher.addURI(PROVIDER_NAME, "weathers/#", WEATHERS_ID);
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        db = mOpenHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(WEATHER_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case WEATHERS:
                qb.setProjectionMap(WEATHER_PROJECTION_MAP);
                break;

            case WEATHERS_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = DATE;
        }
        Cursor cursor = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case WEATHERS:
                return "vnd.android.cursor.dir/vnd.example.weathers";
            case WEATHERS_ID:
                return "vnd.android.cursor.item/vnd.example.weathers";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(	WEATHER_TABLE_NAME, "", values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        try {
            throw new SQLException("Failed to add a record into " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case WEATHERS:
                count = db.delete(WEATHER_TABLE_NAME, selection, selectionArgs);
                break;

            case WEATHERS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( WEATHER_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case WEATHERS:
                count = db.update(WEATHER_TABLE_NAME, values, selection, selectionArgs);
                break;

            case WEATHERS_ID:
                count = db.update(WEATHER_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private class MainDatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_CREATE_MAIN = " CREATE TABLE " + WEATHER_TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " date TEXT NOT NULL, " +
                " temp TEXT NOT NULL);";

        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
            onCreate(db);
        }
    }
}
